package com.example.cryptowalletmanagement.dto.wallet;

import java.math.BigDecimal;
import java.util.List;

public record WalletEvaluationInputDTO(List<AssetInputDTO> assets) {
    public record AssetInputDTO(String symbol, BigDecimal quantity, BigDecimal value) {}
}
