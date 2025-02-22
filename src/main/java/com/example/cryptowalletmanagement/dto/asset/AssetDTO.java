package com.example.cryptowalletmanagement.dto.asset;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
import com.example.cryptowalletmanagement.repository.entities.AssetEntity;

import java.math.BigDecimal;

public record AssetDTO(Long id, WalletDTO wallet, String symbol, BigDecimal quantity, BigDecimal price) {

    public static AssetEntity toAssetEntity(AssetDTO assetDTO) {
        if (assetDTO == null) return null;
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setId(assetDTO.id());
        assetEntity.setWallet(WalletDTO.toWalletEntity(assetDTO.wallet()));
        assetEntity.setSymbol(assetDTO.symbol());
        assetEntity.setQuantity(assetDTO.quantity());
        assetEntity.setPrice(assetDTO.price());
        return assetEntity;
    }

    public static AssetDTO fromAssetEntity(AssetEntity assetEntity) {
        if (assetEntity == null) return null;
        return new AssetDTO(assetEntity.getId(), WalletDTO.fromWalletEntity(assetEntity.getWallet()), assetEntity.getSymbol(), assetEntity.getQuantity(), assetEntity.getPrice());
    }
}
