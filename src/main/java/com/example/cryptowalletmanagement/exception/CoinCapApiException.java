package com.example.cryptowalletmanagement.exception;

public class CoinCapApiException extends RuntimeException {
    public CoinCapApiException(String message) {
        super(message);
    }
    public CoinCapApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
