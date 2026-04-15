package com.example.cryptowalletmanagement.config;

import com.example.cryptowalletmanagement.config.properties.CoinCapApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuration class for setting beans used across the application
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(CoinCapApiProperties.class)
public class ApplicationConfig {

    private final CoinCapApiProperties coinCapApiProperties;

    public ApplicationConfig(CoinCapApiProperties coinCapApiProperties) {
        this.coinCapApiProperties = coinCapApiProperties;
    }

    /**
     * builds a resttemplate with default properties
     * @return
     */
    @Bean
    public RestTemplate coinCapRestTemplate() {
        String baseUrl = Objects.requireNonNull(coinCapApiProperties.getBaseUrl(), "CoinCap base URL must be provided");
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //prevents enconding characters or spaces in the url
        String authorizationHeader = "Bearer " + Objects.requireNonNull(coinCapApiProperties.getApiKey(), "CoinCap API key must be provided");
        return new RestTemplateBuilder()
                .uriTemplateHandler(factory)
                .connectTimeout(Duration.ofMillis(coinCapApiProperties.getConnectTimeout()))
                .defaultHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
