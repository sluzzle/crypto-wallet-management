package com.example.cryptowalletmanagement.service.coincap;

import com.example.cryptowalletmanagement.exception.CoinCapApiException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CoinCapApiClient {
    BigDecimal fetchAssetPrice(String symbol, LocalDate date) throws CoinCapApiException;
    BigDecimal fetchAssetPrice(String symbol) throws CoinCapApiException;
}
