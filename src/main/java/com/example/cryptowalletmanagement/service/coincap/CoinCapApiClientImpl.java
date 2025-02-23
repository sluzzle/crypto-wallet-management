package com.example.cryptowalletmanagement.service.coincap;

import com.example.cryptowalletmanagement.config.properties.CoinCapApiProperties;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Interface for interacting with CoinCapAPI
 */
@Service
public class CoinCapApiClientImpl implements CoinCapApiClient {

    private static final Logger logger = LoggerFactory.getLogger(CoinCapApiClientImpl.class);

    private final RestTemplate restTemplate;
    private final CoinCapApiProperties coinCapApiProperties;

    public CoinCapApiClientImpl(@Qualifier("coinCapRestTemplate") RestTemplate restTemplate, CoinCapApiProperties coinCapApiProperties) {
        this.restTemplate = restTemplate;
        this.coinCapApiProperties = coinCapApiProperties;
    }

    @Override
    public BigDecimal fetchCoinPrice(String symbol) {
        return fetchCoinPrice(symbol, null);
    }

    @Override
    public BigDecimal fetchCoinPrice(String symbol, LocalDate date) {
        String url = String.format(coinCapApiProperties.getSearchQuery(), symbol);
        if (date != null) {
            Long unixTime = this.convertDateToUnixTime(date);
            url = String.format(coinCapApiProperties.getHistoryQuery(), fetchCoinId(symbol), unixTime, unixTime + 5000);
        }
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new CoinCapApiException("unexpected api response for asset: " + symbol);
            }
            JsonNode jsonNode = parseJsonResponse(response);
            if (jsonNode != null && !jsonNode.isEmpty()) {
                return BigDecimal.valueOf(jsonNode.get("priceUsd").asDouble());
            } else {
                logger.error("unable to find price for: {}", symbol);
                throw new CoinCapApiException("unable to find price for: " + symbol);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CoinCapApiException("unexpected api response for asset: " + symbol, e);
        }
    }

    /**
     * @param symbol
     * @return
     * @throws CoinCapApiException
     */
    private String fetchCoinId(String symbol) throws CoinCapApiException {
        ResponseEntity<String> response = restTemplate.getForEntity(String.format(coinCapApiProperties.getSearchQuery(), symbol), String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CoinCapApiException("unable to fetch price from coincap for : " + symbol);
        }
        JsonNode idNode = parseJsonResponse(response);
        if (idNode == null || idNode.isEmpty()) {
            throw new CoinCapApiException("unable to find coin id for: " + symbol);
        }
        return idNode.get("id").asText();
    }

    /**
     * @param response
     * @return
     */
    private JsonNode parseJsonResponse(ResponseEntity<String> response) {
        try {
            String jsonBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);
            if (jsonNode.has("data") && !jsonNode.path("data").isEmpty()) {
                return jsonNode.path("data").get(0);
            } else {
                return null;
            }
        } catch (RuntimeException | JsonProcessingException e) {
            throw new CoinCapApiException("Error parsing coin price response", e);
        }
    }

    /**
     *
     * @param date
     * @return
     */
    private Long convertDateToUnixTime(LocalDate date){
        return date.atStartOfDay(ZoneId.of("UTC")).toInstant()
                .toEpochMilli();
    }

}