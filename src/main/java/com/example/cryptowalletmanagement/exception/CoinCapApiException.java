package com.example.cryptowalletmanagement.exception;

/**
 * exceptions thrown when during interactions with coincap api
 */
public class CoinCapApiException extends RuntimeException {
    public CoinCapApiException(String message) {
        super(message);
    }
    public CoinCapApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
