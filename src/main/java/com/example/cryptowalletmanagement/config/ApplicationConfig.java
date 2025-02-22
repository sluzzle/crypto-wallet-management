package com.example.cryptowalletmanagement.config;

import com.example.cryptowalletmanagement.config.properties.CoinCapApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Locale;

@Configuration
@EnableConfigurationProperties(CoinCapApiProperties.class)
public class ApplicationConfig {

    private final CoinCapApiProperties coinCapApiProperties;

    public ApplicationConfig(CoinCapApiProperties coinCapApiProperties) {
        this.coinCapApiProperties = coinCapApiProperties;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    @Bean
    public RestTemplate coinCapRestTemplate() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(coinCapApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return new RestTemplateBuilder().uriTemplateHandler(factory).build();
    }

}
