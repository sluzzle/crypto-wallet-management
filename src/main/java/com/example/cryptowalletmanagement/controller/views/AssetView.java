package com.example.cryptowalletmanagement.controller.views;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;

import java.math.BigDecimal;

/**
 * a record that represents a simplified view of an asset
 */
public record AssetView(String symbol, BigDecimal quantity, BigDecimal price, BigDecimal value) {
    public static AssetView fromAssetDTO(AssetDTO assetDTO) {
        if (assetDTO == null) return null;
        return new AssetView(assetDTO.symbol(), assetDTO.quantity(), assetDTO.price(), assetDTO.price().multiply(assetDTO.quantity()));
    }
}
