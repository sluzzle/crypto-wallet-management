package com.example.cryptowalletmanagement.dto.wallet;

import com.example.cryptowalletmanagement.repository.entities.WalletEntity;

public record WalletDTO(Long id, String email, String token) {

    public static WalletDTO fromWalletEntity(WalletEntity walletEntity) {
        if (walletEntity == null) {
            return null;
        }
        return new WalletDTO(walletEntity.getId(), walletEntity.getEmail(), walletEntity.getToken());
    }

    public static WalletEntity toWalletEntity(WalletDTO walletDTO) {
        if (walletDTO == null) {
            return null;
        }
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setId(walletDTO.id());
        walletEntity.setEmail(walletDTO.email());
        walletEntity.setToken(walletDTO.token());
        return walletEntity;
    }
}
