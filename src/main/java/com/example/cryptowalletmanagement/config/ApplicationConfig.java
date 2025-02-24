package com.example.cryptowalletmanagement.config;

import com.example.cryptowalletmanagement.config.properties.CoinCapApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

/**
 * Configuration class for setting beans used across the application
 */
@Configuration
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
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(coinCapApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //prevents enconding characters or spaces in the url
        return new RestTemplateBuilder()
                .uriTemplateHandler(factory)
                .connectTimeout(Duration.ofMillis(coinCapApiProperties.getConnectTimeout()))
                .build();
    }

}
