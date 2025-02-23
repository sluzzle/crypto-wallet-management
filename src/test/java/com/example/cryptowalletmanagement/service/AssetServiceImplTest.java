package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.exception.WalletException;
import com.example.cryptowalletmanagement.repository.entities.AssetEntity;
import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import com.example.cryptowalletmanagement.repository.interfaces.AssetRepository;
import com.example.cryptowalletmanagement.repository.interfaces.WalletRepository;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private CoinCapApiClient coinCapApiClient;
    @InjectMocks
    private AssetServiceImpl assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAsset_SuccessfulSave() {
        String walletToken = "token";
        String symbol = "BTC";
        BigDecimal quantity = new BigDecimal("1.5");
        BigDecimal price = new BigDecimal("20000");

        WalletEntity wallet = new WalletEntity();
        wallet.setId(1L);

        when(walletRepository.findWalletEntityByToken(walletToken)).thenReturn(Optional.of(wallet));
        when(coinCapApiClient.fetchAssetPrice(symbol)).thenReturn(price);

        AssetEntity savedAsset = new AssetEntity();
        savedAsset.setId(1L);
        savedAsset.setWallet(wallet);
        savedAsset.setSymbol(symbol);
        savedAsset.setQuantity(quantity);
        savedAsset.setPrice(price);

        when(assetRepository.save(any(AssetEntity.class))).thenReturn(savedAsset);

        AssetDTO assetDTO = assetService.saveAsset(walletToken, symbol, quantity);

        assertNotNull(assetDTO);
        assertEquals(savedAsset.getId(), assetDTO.id());
        assertEquals(savedAsset.getSymbol(), assetDTO.symbol());
        assertEquals(savedAsset.getQuantity(), assetDTO.quantity());
    }

    @Test
    void saveAsset_WalletNotFound() {
        String walletToken = "invalid-token";
        String symbol = "BTC";
        BigDecimal quantity = new BigDecimal("1.5");

        when(walletRepository.findWalletEntityByToken(walletToken)).thenReturn(Optional.empty());

        WalletException exception = assertThrows(WalletException.class, () ->
                assetService.saveAsset(walletToken, symbol, quantity)
        );

        assertEquals("Wallet not found for token : " + walletToken, exception.getMessage());
        verify(assetRepository, never()).save(any(AssetEntity.class));
    }


    @Test
    void getAllAssets_ReturnsAssetDTOList() {
        AssetEntity asset1 = new AssetEntity();
        asset1.setId(1L);
        asset1.setSymbol("BTC");
        asset1.setQuantity(new BigDecimal("1.5"));

        AssetEntity asset2 = new AssetEntity();
        asset2.setId(2L);
        asset2.setSymbol("ETH");
        asset2.setQuantity(new BigDecimal("2.0"));

        when(assetRepository.findAll()).thenReturn(List.of(asset1, asset2));

        List<AssetDTO> assets = assetService.getAllAssets("some-wallet-id");

        assertNotNull(assets);
        assertEquals(2, assets.size());
        assertEquals("BTC", assets.get(0).symbol());
        assertEquals("ETH", assets.get(1).symbol());
    }
}