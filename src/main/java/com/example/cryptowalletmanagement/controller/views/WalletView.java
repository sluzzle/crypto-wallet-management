package com.example.cryptowalletmanagement.controller.views;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;

public record WalletView(String email, String token) {
    public static WalletView fromWalletDTO(WalletDTO walletDTO) {
        return new WalletView(walletDTO.email(), walletDTO.token());
    }
}
