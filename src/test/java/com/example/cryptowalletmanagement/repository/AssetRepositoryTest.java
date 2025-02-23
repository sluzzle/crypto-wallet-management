package com.example.cryptowalletmanagement.repository;

import com.example.cryptowalletmanagement.repository.entities.AssetEntity;
import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import com.example.cryptowalletmanagement.repository.interfaces.AssetRepository;
import com.example.cryptowalletmanagement.repository.interfaces.WalletRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AssetRepositoryTest {

    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private WalletRepository walletRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private AssetEntity asset1;
    private AssetEntity asset2;

    @BeforeEach
    void setUp() {
        WalletEntity wallet = new WalletEntity();
        wallet.setToken("wallet1");
        wallet.setEmail("test@test.com");
        wallet.setToken("token");

        wallet = walletRepository.save(wallet);

        asset1 = new AssetEntity();
        asset1.setSymbol("BTC");
        asset1.setPrice(BigDecimal.valueOf(25000.0));
        asset1.setWallet(wallet);
        asset1.setQuantity(BigDecimal.valueOf(1.5));
        asset1.setWallet(wallet);

        asset2 = new AssetEntity();
        asset2.setSymbol("ETH");
        asset2.setPrice(BigDecimal.valueOf(1800.0));
        asset1.setWallet(wallet);
        asset2.setQuantity(BigDecimal.valueOf(3.0));
        asset2.setWallet(wallet);

        assetRepository.save(asset1);
        assetRepository.save(asset2);

        entityManager.clear();
    }

    @Test
    @Rollback
    void findAllSymbolsByWallet_ShouldReturnAllSymbolsByWallet() {
        String wallet = "wallet1";

        List<String> symbols = assetRepository.findAllSymbolsByWallet(wallet);

        assertThat(symbols).isNotEmpty()
               .containsExactlyInAnyOrder("BTC", "ETH");
    }

    @Test
    @Rollback
    void updatePriceBySymbol_ShouldUpdateAssetPriceBySymbol() {

        String symbol = "BTC";
        Double newPrice = 30000.0;

        int rowsUpdated = assetRepository.updatePriceBySymbol(symbol, BigDecimal.valueOf(newPrice));

        assertThat(rowsUpdated).isEqualTo(1);

        AssetEntity result = assetRepository.findBySymbol(symbol).orElseThrow();
        assertThat(result.getPrice()).isEqualTo(newPrice);
    }

    @Test
    @Rollback
    void findAllByWalletToken_ShouldGetAllAssetsByWalletToken() {

        String walletToken = "token";

        List<AssetEntity> assets = assetRepository.findAllByWalletToken(walletToken);
        assertThat(assets).isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Rollback
    void findAllDistinctSymbols_ShouldGetAllDistinctSymbols() {

        List<String> assetSymbols = assetRepository.findAllDistinctSymbols();
        assertThat(assetSymbols).isNotEmpty();
        assertThat(assetSymbols.size()).isEqualTo(2);
    }
}