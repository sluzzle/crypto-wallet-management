package com.example.cryptowalletmanagement.service.coincap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinPrice (Long id, String symbol, Double price) {}
