package com.example.cryptowalletmanagement.dto.wallet;

import java.math.BigDecimal;

public record WalletEvaluationOutputDTO (BigDecimal total, String bestAsset, double bestPerformance, String worstAsset, double worstPerformance) {}

