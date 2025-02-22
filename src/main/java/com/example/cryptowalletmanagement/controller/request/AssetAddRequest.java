package com.example.cryptowalletmanagement.controller.request;

import java.math.BigDecimal;

public record AssetAddRequest (String walletToken, String symbol, BigDecimal price, BigDecimal quantity) {
}
