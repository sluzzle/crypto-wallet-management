package com.example.cryptowalletmanagement.controller.views;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;

/**
 * a record that represents a simplified view of an wallet
 * @param email
 * @param token
 */
public record WalletView(String email, String token) {
    public static WalletView fromWalletDTO(WalletDTO walletDTO) {
        return new WalletView(walletDTO.email(), walletDTO.token());
    }
}
