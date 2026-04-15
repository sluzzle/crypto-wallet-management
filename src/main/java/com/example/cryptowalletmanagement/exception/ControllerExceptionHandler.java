package com.example.cryptowalletmanagement.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Global exception handler for controllers in the application
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * Handles generic Exceptions (checked exceptions).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        logger.error("Unhandled exception in request processing", e);
        return makeErrorResponse("An internal error occurred", null, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles WalletException
     *
     * @param e
     * @param request
     * @return a ResponseEntity
     */
    @ExceptionHandler(WalletException.class)
    public ResponseEntity<Object> handleWalletException(WalletException e, WebRequest request) {
        return makeErrorResponse(e.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles AssetException
     *
     * @param e
     * @param request
     * @return a ResponseEntity
     */
    @ExceptionHandler(AssetException.class)
    public ResponseEntity<Object> handleAssetException(AssetException e, WebRequest request) {
        return makeErrorResponse(e.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles CoinCapApiException
     *
     * @param e
     * @param request
     * @return a ResponseEntity
     */
    @ExceptionHandler(CoinCapApiException.class)
    public ResponseEntity<Object> handleCoinCapApiException(CoinCapApiException e, WebRequest request) {
        return makeErrorResponse(e.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException
     *
     * @param e
     * @param request
     * @return a ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleAssetException(MethodArgumentNotValidException e, WebRequest request) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return makeErrorResponse(errors, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param message
     * @param request
     * @param status
     * @return
     */
    private ResponseEntity<Object> makeErrorResponse(Object message, WebRequest request, HttpStatus status) {
        Map<String, Object> body = Map.of(
                "request", request != null ? request.getDescription(false): "unknown",
                "message", message,
                "timestamp", System.currentTimeMillis()
        );
        return new ResponseEntity<>(body, Objects.requireNonNull(status, "HTTP status must not be null"));
    }
}
