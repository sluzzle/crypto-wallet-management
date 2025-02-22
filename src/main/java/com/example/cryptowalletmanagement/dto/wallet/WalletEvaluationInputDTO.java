package com.example.cryptowalletmanagement.dto.wallet;

import java.util.List;

public record WalletEvaluationInputDTO(List<AssetInputDTO> assets) {
    public record AssetInputDTO(String symbol,Double quantity,Double value) {}
}
