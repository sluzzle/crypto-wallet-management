package com.example.cryptowalletmanagement.dto.wallet;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record WalletEvaluationOutputDTO (BigDecimal total, String bestAsset, @JsonFormat(pattern="#0.00") double bestPerformance, String worstAsset, double worstPerformance) {}

