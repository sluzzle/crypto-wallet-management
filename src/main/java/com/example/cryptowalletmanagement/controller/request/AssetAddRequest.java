package com.example.cryptowalletmanagement.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AssetAddRequest(
        @NotBlank(message = "Wallet token must not be blank")
        String walletToken,
        @NotBlank(message = "Symbol must not be blank")
        String symbol,
        @NotNull(message = "Quantity must not be null")
        BigDecimal quantity
) {
}
