package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.exception.AssetException;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.exception.WalletException;
import com.example.cryptowalletmanagement.repository.entities.AssetEntity;
import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import com.example.cryptowalletmanagement.repository.interfaces.AssetRepository;
import com.example.cryptowalletmanagement.repository.interfaces.WalletRepository;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing crypto wallet assets
 */
@Service
public class AssetServiceImpl implements AssetService{

    private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);
    private final AssetRepository assetRepository;
    private final WalletRepository walletRepository;
    private final CoinCapApiClient coinCapApiClient;

    public AssetServiceImpl(AssetRepository assetRepository, WalletRepository walletRepository, CoinCapApiClient coinCapApiClient) {
        this.assetRepository = assetRepository;
        this.walletRepository = walletRepository;
        this.coinCapApiClient = coinCapApiClient;
    }

    /**
     * Saves an asset to a specific wallet
     *
     * @param walletToken
     * @param symbol (BTC, ETH...)
     * @param quantity
     * @return An AssetDTO
     * @throws WalletException
     */
    @Override
    public AssetDTO saveAsset(String walletToken, String symbol, BigDecimal quantity) {
        WalletEntity wallet = walletRepository.findWalletEntityByToken(walletToken)
                .orElseThrow(() -> new WalletException("Wallet not found for token : " + walletToken));
        BigDecimal assetPrice;
        try {
            assetPrice = coinCapApiClient.fetchAssetPrice(symbol);
            logger.debug("Fetched price for asset: {} is {}", symbol, assetPrice);
            if (assetPrice == null) {
                throw new AssetException("Invalid price for asset : " + symbol);
            }
        } catch (CoinCapApiException e) {
            throw new AssetException("Unable to fetch price for asset : " + symbol);
        }

        Optional<AssetEntity> existingAsset = assetRepository.findBySymbolAndWalletToken(symbol, wallet.getToken());

        if (existingAsset.isPresent()) {
            existingAsset.get().setQuantity(existingAsset.get().getQuantity().add(quantity));
            return AssetDTO.fromAssetEntity(assetRepository.save(existingAsset.get()));
        } else {
            AssetEntity asset = new AssetEntity();
            asset.setWallet(wallet);
            asset.setSymbol(symbol);
            asset.setQuantity(quantity);
            asset.setPrice(assetPrice);
            return AssetDTO.fromAssetEntity(assetRepository.save(asset));
        }
    }

    /**
     * Retrieves all assets that belongs to q wallet
     *
     * @param walletId
     * @return A list of AssetDTO objects
     */
    @Override
    public List<AssetDTO> getAllAssets(String walletId) {
        return assetRepository.findAll().stream().map(AssetDTO::fromAssetEntity).toList();
    }
}