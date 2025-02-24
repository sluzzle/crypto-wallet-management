package com.example.cryptowalletmanagement.exception;

/**
 * exceptions thrown when operating with Wallet
 */
public class WalletException extends RuntimeException {
    public WalletException(String message) {
        super(message);
    }
    public WalletException(String message, Throwable cause) {
        super(message, cause);
    }
}
