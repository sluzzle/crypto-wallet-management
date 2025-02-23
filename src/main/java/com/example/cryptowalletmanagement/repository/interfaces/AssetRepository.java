package com.example.cryptowalletmanagement.repository.interfaces;

import com.example.cryptowalletmanagement.repository.entities.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    Optional<AssetEntity> findBySymbol(String symbol);

    Optional<AssetEntity> findBySymbolAndWalletToken(String symbol, String token);

    @Query(value = "SELECT DISTINCT a.symbol FROM assets a", nativeQuery = true)
    List<String> findAllDistinctSymbols();

    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query(value = "UPDATE assets a SET a.price = :price WHERE a.symbol = :symbol", nativeQuery = true)
    int updatePriceBySymbol(@Param("symbol") String symbol, @Param("price") BigDecimal price);

    @Query(value = "SELECT a.symbol FROM assets a INNER JOIN wallets w ON a.wallet_id = w.id WHERE w.token = :token ", nativeQuery = true)
    List<String> findAllSymbolsByWallet(@Param("token") String walletToken);

    List<AssetEntity> findAllByWalletToken(String walletToken);
}
