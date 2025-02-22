package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
import com.example.cryptowalletmanagement.exception.WalletException;
import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import com.example.cryptowalletmanagement.repository.interfaces.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWallet_shouldCreateWalletSuccessfully() throws WalletException {

        WalletDTO walletDTO = new WalletDTO(null, "test@test.com", UUID.randomUUID().toString());

        WalletEntity walletEntity = WalletDTO.toWalletEntity(walletDTO);

        when(walletRepository.findWalletEntityByEmail(walletDTO.email())).thenReturn(Optional.empty());
        when(walletRepository.save(any(WalletEntity.class))).thenReturn(walletEntity);

        WalletDTO result = walletService.createWallet(walletDTO.email());

        assertNotNull(result);
        assertEquals(walletDTO.email(), result.email());
    }

    @Test
    void createWallet_shouldThrowWalletException_whenEmailAlreadyExists() {

        WalletDTO existingWallet = new WalletDTO(null, "test@test.com", UUID.randomUUID().toString());

        when(walletRepository.findWalletEntityByEmail(existingWallet.email())).thenReturn(Optional.of(WalletDTO.toWalletEntity(existingWallet)));

        WalletException exception = assertThrows(WalletException.class, () -> walletService.createWallet(existingWallet.email()));
        assertEquals("Wallet already exists for this email.", exception.getMessage());
        verify(walletRepository, times(1)).findWalletEntityByEmail(existingWallet.email());
    }

}
