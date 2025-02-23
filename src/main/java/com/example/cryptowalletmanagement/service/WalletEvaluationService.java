package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;

import java.time.LocalDate;

public interface WalletEvaluationService {
    WalletEvaluationOutputDTO evaluateWallet(WalletEvaluationInputDTO walletEvaluationInput, LocalDate date) throws CoinCapApiException;
}
