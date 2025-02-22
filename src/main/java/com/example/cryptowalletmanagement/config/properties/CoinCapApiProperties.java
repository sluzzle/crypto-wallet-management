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

    public CoinCapApiProperties(String baseUrl, String searchQuery, String historyQuery) {
        this.baseUrl = baseUrl;
        this.searchQuery = searchQuery;
        this.historyQuery = historyQuery;
    }
}
