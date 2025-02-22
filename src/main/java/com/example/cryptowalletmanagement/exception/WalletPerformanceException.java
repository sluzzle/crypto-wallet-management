package com.example.cryptowalletmanagement.exception;

public class WalletPerformanceException extends RuntimeException {
    public WalletPerformanceException(String message) {
        super(message);
    }
    public WalletPerformanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
