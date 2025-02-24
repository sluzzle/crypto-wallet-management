package com.example.cryptowalletmanagement.repository.interfaces;

import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findWalletEntityByEmail(String email);
    Optional<WalletEntity> findWalletEntityByToken(String token);
}
