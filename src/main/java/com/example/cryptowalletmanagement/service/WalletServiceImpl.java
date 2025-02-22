package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
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
import java.util.Optional;
import java.util.UUID;

/**
 * Wallet service that manages the operations on the Wallet.
 */
@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;
    private final AssetRepository assetRepository;
    private final CoinCapApiClient coinCapApiClient;

    public WalletServiceImpl(WalletRepository walletRepository, AssetRepository assetRepository, CoinCapApiClient coinCapApiClient) {
        this.walletRepository = walletRepository;
        this.assetRepository = assetRepository;
        this.coinCapApiClient = coinCapApiClient;
    }

    /**
     * creates a new wallet from a given email
     */
    @Override
    public WalletDTO createWallet(String email) throws WalletException{
        if (walletRepository.findWalletEntityByEmail(email).isPresent()) {
            logger.debug("Wallet already exists for this email {} ", email);
            throw new WalletException("Wallet already exists for this email.");
        }
        WalletEntity wallet = new WalletEntity();
        wallet.setEmail(email);
        wallet.setToken(UUID.randomUUID().toString());
        return WalletDTO.fromWalletEntity(walletRepository.save(wallet));
    }

    @Override
    public WalletDTO getWalletByToken(String walletToken) throws WalletException {
        Optional<WalletEntity> wallet = walletRepository.findWalletEntityByToken(walletToken);
        if(wallet.isEmpty()){
            throw new WalletException("wallet not found for token: " + walletToken);
        }
        return WalletDTO.fromWalletEntity(wallet.get());
    }


    public AssetDTO addAssetToWallet(String token, String symbol, BigDecimal price, BigDecimal quantity) throws WalletException, CoinCapApiException {
        Optional<WalletEntity> wallet = walletRepository.findWalletEntityByToken(token);
        if(wallet.isEmpty()){
            throw new WalletException("Wallet not found for token: " + token);
        }

        BigDecimal tokenPrice = coinCapApiClient.fetchCoinPrice(symbol);
        if (tokenPrice == null || tokenPrice.compareTo(price) != 0) {
            throw new WalletException("Invalid price or token: " + symbol);
        }

        AssetEntity asset = new AssetEntity();
        asset.setWallet(wallet.get());
        asset.setSymbol(symbol);
        asset.setPrice(tokenPrice);
        asset.setQuantity(quantity);
        return AssetDTO.fromAssetEntity(assetRepository.save(asset));
    }
}
