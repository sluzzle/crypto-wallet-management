package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.exception.WalletException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for managing crypto wallet assets
 */
public interface AssetService {
    /**
     * Saves an asset to a specific wallet
     *
     * @param walletToken
     * @param symbol (BTC, ETH...)
     * @param quantity
     * @return An AssetDTO
     * @throws WalletException
     * */
    AssetDTO saveAsset(String walletToken, String symbol, BigDecimal quantity);

    /**
     * Retrieves all assets that belongs to q wallet
     *
     * @param walletId
     * @return A list of AssetDTO objects
     */
    List<AssetDTO> getAllAssets(String walletId);
}
