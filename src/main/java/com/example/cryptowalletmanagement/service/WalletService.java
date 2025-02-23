package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;

public interface WalletService {
    WalletDTO createWallet(String email);
    WalletDTO getWalletByToken(String walletToken);
}
