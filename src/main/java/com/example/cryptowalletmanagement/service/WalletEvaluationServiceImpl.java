package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.AssetPerformance;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.exception.WalletPerformanceException;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO.AssetInputDTO;

/**
 * Service for evauating the performance of a given assets in a crypto wallet
 */
@Service
public class WalletEvaluationServiceImpl implements WalletEvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(WalletEvaluationServiceImpl.class);
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
        logger.debug("Starting wallet evaluation for date: {} with input assets: {}", date, walletEvaluationInput.assets());
        List<AssetPerformance> performances = walletEvaluationInput.assets().stream()
                .map(asset -> {
                    BigDecimal pastPrice = this.getCurrentPrice(asset.symbol(), date);
                    double performance = calculatePerformance(asset, pastPrice).doubleValue();
                    logger.debug("Asset performance calculated: asset={}, performance={}", asset.symbol(), performance);
                    return new AssetPerformance(asset.symbol(), performance);
                }).toList();

        AssetPerformance bestAsset = performances.stream()
                .max(Comparator.comparingDouble(AssetPerformance::performance))
                .orElse(new AssetPerformance("", 0));
        AssetPerformance worstAsset = performances.stream()
                .min(Comparator.comparingDouble(AssetPerformance::performance))
                .orElse(new AssetPerformance("", 0));
        return new WalletEvaluationOutputDTO(totalValue, bestAsset.symbol(), bestAsset.performance(), worstAsset.symbol(), worstAsset.performance());
    }

    /**
     * calculates the performance of an asset as a percentage
     *
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
    private BigDecimal calculateTotal(WalletEvaluationInputDTO walletEvaluationInput, LocalDate date) {
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
    private BigDecimal getCurrentPrice(String symbol, LocalDate date) {
        BigDecimal price = coinCapApiClient.fetchAssetPrice(symbol, date);
        if (price == null) {
            throw new WalletPerformanceException("Unable to fetch price for asset: " + symbol);
        }
        return price;
    }

}