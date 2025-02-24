package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
import com.example.cryptowalletmanagement.exception.WalletException;
import com.example.cryptowalletmanagement.repository.entities.WalletEntity;
import com.example.cryptowalletmanagement.repository.interfaces.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Wallet service that manages the operations on the Wallet.
 */
@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * creates a new wallet from a given email
     * @param email
     * @return
     * @throws WalletException
     */
    @Override
    public WalletDTO createWallet(String email) throws WalletException{
        if (walletRepository.findWalletEntityByEmail(email).isPresent()) {
            logger.debug("wallet creation failed, email already exists {} ", email);
            throw new WalletException("Wallet already exists for this email " + email);
        }
        WalletEntity wallet = new WalletEntity();
        wallet.setEmail(email);
        wallet.setToken(UUID.randomUUID().toString());
        return WalletDTO.fromWalletEntity(walletRepository.save(wallet));
    }

    /**
     * retreives a wallet detail by a given token
     * @param walletToken
     * @return
     * @throws WalletException
     */
    @Override
    public WalletDTO getWalletByToken(String walletToken) throws WalletException {
        Optional<WalletEntity> wallet = walletRepository.findWalletEntityByToken(walletToken);
        if(wallet.isEmpty()){
            throw new WalletException("wallet not found for token: " + walletToken);
        }
        return WalletDTO.fromWalletEntity(wallet.get());
    }
}
