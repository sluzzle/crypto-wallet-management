package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.exception.WalletPerformanceException;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

import static com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO.AssetInputDTO;

@Service
public class WalletEvaluationServiceImpl implements WalletEvaluationService {

    private final CoinCapApiClient coinCapApiClient;
    private final MathContext precision;

    public WalletEvaluationServiceImpl(CoinCapApiClient coinCapApiClient) {
        this.coinCapApiClient = coinCapApiClient;
        precision = new MathContext(4, RoundingMode.HALF_UP);
    }


    /**
     * calculates the wallet performance of given assets
     *
     * @param walletEvaluationInput
     * @param date
     * @return
     * @throws CoinCapApiException
     */
    @Override
    public WalletEvaluationOutputDTO evaluateWallet(WalletEvaluationInputDTO walletEvaluationInput, LocalDate date) {
        BigDecimal totalValue = this.calculateTotal(walletEvaluationInput, date);
        String bestAsset = "";
        double bestPerformance = 0;
        String worstAsset = "";
        double worstPerformance =0;

        for (AssetInputDTO asset : walletEvaluationInput.assets()) {
            BigDecimal price = this.getCurrentPrice(asset.symbol(), date);
            double performance = calculatePerformance(asset, price).doubleValue();

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
     * @param asset
     * @param pastPrice
     * @return
     */
    private BigDecimal calculatePerformance(AssetInputDTO asset, BigDecimal pastPrice) {
        BigDecimal assetPrice = asset.value().divide(asset.quantity(), precision);
        return (pastPrice.subtract(assetPrice)).divide(assetPrice, precision).multiply(BigDecimal.valueOf(100));
    }

    /**
     * calculates the total of a given assets
     *
     * @param walletEvaluationInput
     * @param date
     * @return
     * @throws CoinCapApiException
     */
    public BigDecimal calculateTotal(WalletEvaluationInputDTO walletEvaluationInput, LocalDate date) {
        return walletEvaluationInput.assets().stream()
                .map(asset -> {
                    BigDecimal price = this.getCurrentPrice(asset.symbol(), date);
                    return price.multiply(asset.quantity(), precision);
                })
                .reduce(BigDecimal::add).orElseThrow(() -> new WalletPerformanceException("unable to calculate total value"));
    }

    /**
     * fetches the price of an asset from the coincap api
     *
     * @param symbol
     * @return
     */
    public BigDecimal getCurrentPrice(String symbol, LocalDate date) {
        try {
            BigDecimal price = coinCapApiClient.fetchCoinPrice(symbol, date);
            if (price == null) {
                throw new WalletPerformanceException("Unable to fetch price for asset: " + symbol);
            }
            return price;
        } catch (CoinCapApiException e) {
            throw new WalletPerformanceException("Unable to fetch price for asset: " + symbol);
        }
    }

}