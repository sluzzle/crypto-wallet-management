package com.example.cryptowalletmanagement.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findWalletEntityByEmail(String email);
    Optional<WalletEntity> findWalletEntityByToken(String token);}
