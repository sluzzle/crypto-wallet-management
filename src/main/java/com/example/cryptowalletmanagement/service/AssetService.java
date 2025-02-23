package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AssetService {
    AssetDTO saveAsset(String walletId, String symbol, BigDecimal quantity);
    List<AssetDTO> getAllAssets(String walletId);
}
