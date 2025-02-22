package com.example.cryptowalletmanagement.dto.wallet;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;

import java.math.BigDecimal;
import java.util.List;

public record WalletDetailDTO(Long id, BigDecimal total, List<AssetValueDTO> assets) {
    public record AssetValueDTO(String symbol, BigDecimal quantity, BigDecimal price, BigDecimal value) {
    }
    public static WalletDetailDTO of(Long id, BigDecimal total, List<AssetDTO> assets) {
        List<AssetValueDTO> assetValueDTOs = assets.stream()
                .map(asset -> new AssetValueDTO(
                        asset.symbol(),
                        asset.quantity(),
                        asset.price(),
                        asset.price().multiply(asset.quantity())
                ))
                .toList();
        return new WalletDetailDTO(id, total, assetValueDTOs);
    }
}
