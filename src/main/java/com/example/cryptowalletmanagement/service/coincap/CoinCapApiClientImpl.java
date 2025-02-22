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
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
    public BigDecimal fetchCoinPrice(String symbol) throws CoinCapApiException {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(String.format(coinCapApiProperties.getSearchQuery(), symbol), String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new CoinCapApiException("unexpected api response for asset: " + symbol);
            }
            String jsonBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonBody);
            JsonNode priceNode = root.path("data").get(0).get("priceUsd");
            if (!priceNode.isNull()) {
                return BigDecimal.valueOf(priceNode.asDouble());
            } else {
                logger.error("unable to find price for: {}", symbol);
                throw new CoinCapApiException("unable to find price for: " + symbol);
            }

        } catch (RuntimeException | JsonProcessingException  e) {
            throw new CoinCapApiException("Error parsing coin price response for symbol: " + symbol);
        }
    }

    @Override
    public BigDecimal fetchCoinPrice(String symbol, String date) throws CoinCapApiException {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        long startTimestamp = localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        return null;
    }

}