package com.example.cryptowalletmanagement.service.coincap;

import com.example.cryptowalletmanagement.exception.CoinCapApiException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface for interacting with the CoinCap API
 */
public interface CoinCapApiClient {
    /**
     * retrieves the price of a specific asset for a given date from the CoinCap API
     * @param symbol (BTC, ETH..)
     * @param date
     * @return a {@link BigDecimal}
     * @throws CoinCapApiException
     */
    BigDecimal fetchAssetPrice(String symbol, LocalDate date) throws CoinCapApiException;

    /**
     * retrieves the current price of a specific asset from the CoinCap API
     *
     * @param symbol (BTC, ETH..)
     * @return a {@link BigDecimal*/
    BigDecimal fetchAssetPrice(String symbol) throws CoinCapApiException;
}
