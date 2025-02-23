package com.example.cryptowalletmanagement.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for CoinCapAPI
 */
@ConfigurationProperties(prefix = "coincap.api")
@Getter
@Setter
public class CoinCapApiProperties {

    private final String baseUrl;
    private final String searchQuery;
    private final String historyQuery;
    private final long connectTimeout;

    public CoinCapApiProperties(String baseUrl, String searchQuery, String historyQuery, long connectTimeout) {
        this.baseUrl = baseUrl;
        this.searchQuery = searchQuery;
        this.historyQuery = historyQuery;
        this.connectTimeout = connectTimeout;
    }
}
