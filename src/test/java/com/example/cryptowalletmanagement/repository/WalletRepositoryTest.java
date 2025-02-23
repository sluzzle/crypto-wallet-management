package com.example.cryptowalletmanagement.repository;

import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import com.example.cryptowalletmanagement.repository.interfaces.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void findWalletEntityByEmail_ShouldFindWalletEntityByEmail() {
        WalletEntity wallet = new WalletEntity();
        wallet.setEmail("test@test.com");
        wallet.setToken("test_token");
        walletRepository.save(wallet);

        Optional<WalletEntity> foundWallet = walletRepository.findWalletEntityByEmail("test@test.com");

        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getEmail()).isEqualTo("test@test.com");
        assertThat(foundWallet.get().getToken()).isEqualTo("test_token");
    }

    @Test
    void findWalletEntityByToken_ShouldFindWalletEntityByToken() {
        WalletEntity wallet = new WalletEntity();
        wallet.setEmail("test@test.com");
        wallet.setToken("test_token");
        walletRepository.save(wallet);

        Optional<WalletEntity> foundWallet = walletRepository.findWalletEntityByToken("test_token");

        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getEmail()).isEqualTo("test@test.com");
        assertThat(foundWallet.get().getToken()).isEqualTo("test_token");
    }

    @Test
    void findWalletEntityByEmail_ShouldReturnEmptyOptionalIfEmailNotFound() {
        Optional<WalletEntity> foundWallet = walletRepository.findWalletEntityByEmail("not_existent@test.com");

        assertThat(foundWallet).isNotPresent();
    }

    @Test
    void findWalletEntityByToken_ShouldReturnEmptyOptionalIfTokenNotFound() {
        Optional<WalletEntity> foundWallet = walletRepository.findWalletEntityByToken("not_existing_token");

        assertThat(foundWallet).isNotPresent();
    }
}
