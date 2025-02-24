package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;

import java.time.LocalDate;

/**
 * service for evaluating the performance of a crypto wallet
 */

public interface WalletEvaluationService {
    /**
     * calculates the wallet performance of given assets
     *
     * @param walletEvaluationInput
     * @param date
     * @return
     * @throws CoinCapApiException
     */
    WalletEvaluationOutputDTO evaluateWallet(WalletEvaluationInputDTO walletEvaluationInput, LocalDate date) throws CoinCapApiException;
}
