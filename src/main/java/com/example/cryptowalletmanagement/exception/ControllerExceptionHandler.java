package com.example.cryptowalletmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for controllers in the application
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handles WalletException
     * @param e
     * @param request
     * @return a ResponseEntity
     */
    @ExceptionHandler(WalletException.class)
    public ResponseEntity<Object> handleAlreadyExistException(WalletException e, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("message", e.getMessage());
        body.put("request", request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles AssetException
     * @param e
     * @param request
     * @return a ResponseEntity
     */
    @ExceptionHandler(AssetException.class)
    public ResponseEntity<Object> handleAssetException(AssetException e, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("message", e.getMessage());
        body.put("request", request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles CoinCapApiException
     * @param e
     * @param request
     * @return a ResponseEntity
     */
    @ExceptionHandler(CoinCapApiException.class)
    public ResponseEntity<Object> handleAssetException(CoinCapApiException e, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("message", e.getMessage());
        body.put("request", request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
