package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;

/**
 * Interface for managing a wallet
 */
public interface WalletService {
    /**
     * creaets a wallet for a given email
     * @param email
     * @return
     */
    WalletDTO createWallet(String email);

    /**
     * retreives a wallet for a given token
     * @param walletToken
     * @return
     */
    WalletDTO getWalletByToken(String walletToken);
}
