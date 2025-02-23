package com.example.cryptowalletmanagement.service.coincap;

import com.example.cryptowalletmanagement.exception.CoinCapApiException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CoinCapApiClient {
    BigDecimal fetchCoinPrice(String symbol, LocalDate date) throws CoinCapApiException;
    BigDecimal fetchCoinPrice(String symbol) throws CoinCapApiException;
}
