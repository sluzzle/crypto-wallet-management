package com.example.cryptowalletmanagement.exception;

/**
 * exceptions thrown when operating with assets
 */
public class AssetException extends RuntimeException {
    public AssetException(String message) {
        super(message);
    }
    public AssetException(String message, Throwable cause) {
        super(message, cause);
    }
}
