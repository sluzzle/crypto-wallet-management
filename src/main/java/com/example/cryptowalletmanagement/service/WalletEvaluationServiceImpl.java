package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service that evaluates a wallet evolution today or in a specific date in the past
 */
@Service
public class WalletEvaluationServiceImpl implements WalletEvaluationService {

    private final CoinCapApiClient coinCapApiClient;

    public WalletEvaluationServiceImpl(CoinCapApiClient coinCapApiClient) {
        this.coinCapApiClient = coinCapApiClient;
    }

    /**
     * Evaluates a given wallet by the given date
     * @param walletEvaluationInput
     * @param date
     * @return WalletEvaluationOutputDTO object
     */
    @Override
    public WalletEvaluationOutputDTO evaluateWallet(WalletEvaluationInputDTO walletEvaluationInput, String date) throws CoinCapApiException {
        BigDecimal totalValue = new BigDecimal(0);
        String bestAsset = null;
        double bestPerformance = 0;
        String worstAsset = null;
        double worstPerformance =0;

        for (WalletEvaluationInputDTO.AssetInputDTO asset : walletEvaluationInput.assets()) {
            BigDecimal pastPrice = coinCapApiClient.fetchCoinPrice(asset.symbol(), date);
            double performance = calculatePerformance(asset, pastPrice).doubleValue();

            totalValue = totalValue.add(pastPrice.multiply(BigDecimal.valueOf(asset.quantity())));

            if (performance > bestPerformance) {
                bestPerformance = performance;
                bestAsset = asset.symbol();
            }

            if (performance < worstPerformance) {
                worstPerformance = performance;
                worstAsset = asset.symbol();
            }
        }

        return new WalletEvaluationOutputDTO(totalValue, bestAsset, bestPerformance, worstAsset, worstPerformance);
    }

    /**
     * Calculates the performance of an asset
     * @param asset
     * @param pastPrice
     * @return A BigDecimal
     */
    private BigDecimal calculatePerformance(WalletEvaluationInputDTO.AssetInputDTO asset, BigDecimal pastPrice) {
        BigDecimal currentPrice = BigDecimal.valueOf(asset.value() / asset.quantity());
        return (currentPrice.subtract(pastPrice)).divide(pastPrice, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

}