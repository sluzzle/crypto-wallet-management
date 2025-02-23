package com.example.cryptowalletmanagement.service.coincap;

import com.example.cryptowalletmanagement.config.properties.CoinCapApiProperties;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class CoinCapApiClientImplTest {

    private CoinCapApiClient coinCapApiClient;
    @Autowired
    private CoinCapApiProperties coinCapApiProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(coinCapApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        coinCapApiClient = new CoinCapApiClientImpl(new RestTemplateBuilder().uriTemplateHandler(factory).build(),coinCapApiProperties);
    }

    @Test
    void fetchCoinPrice_ShouldReturnCorrectPrice() throws CoinCapApiException {
        String symbol = "BTC";

        BigDecimal price = coinCapApiClient.fetchCoinPrice(symbol);

        assertNotNull(price, "Price should not be null");
        assertTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price should be greater than 0");
    }

    @Test
    void fetchCoinPrice_ShouldReturnCorrectPriceByDate() throws CoinCapApiException {
        String symbol = "BTC";

        LocalDate date = LocalDate.parse("01/02/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy"));


        BigDecimal price = coinCapApiClient.fetchCoinPrice(symbol, date);

        assertNotNull(price, "Price should not be null");
        assertTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price should be greater than 0");
    }

    @Test
    void fetchCoinPrice_ShouldThrowExceptionWhenDateIsInvalid() throws CoinCapApiException {
        String symbol = "BTC";

        LocalDate date = LocalDate.parse("01/02/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

       assertThrows(CoinCapApiException.class, () -> coinCapApiClient.fetchCoinPrice(symbol, date));
    }

    @Test
    void fetchCoinPrice_ShouldThrowCoinCapApiExceptionOnUnknownSymbol() {
        String symbol = "unknown";
        assertThrows(CoinCapApiException.class, () -> coinCapApiClient.fetchCoinPrice(symbol));
    }
}