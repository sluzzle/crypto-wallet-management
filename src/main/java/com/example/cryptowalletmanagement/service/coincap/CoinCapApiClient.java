package com.example.cryptowalletmanagement.service.coincap;

import com.example.cryptowalletmanagement.exception.CoinCapApiException;

import java.math.BigDecimal;

public interface CoinCapApiClient {
    BigDecimal fetchCoinPrice(String symbol, String date) throws CoinCapApiException;
    BigDecimal fetchCoinPrice(String symbol) throws CoinCapApiException;
}
