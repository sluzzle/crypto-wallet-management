package com.example.cryptowalletmanagement.scheduler;

import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.repository.interfaces.AssetRepository;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * a Task to update the price of an asset
 */
@Component
public class PriceUpdateTask implements SchedulingTask<List<CompletableFuture<Void>>> {
    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateTask.class);

    private static final int MAX_THREADS = 3;
    private final ExecutorService executorService;
    private final CoinCapApiClient coinCapApiClient;
    private final AssetRepository assetRepository;

    public PriceUpdateTask(CoinCapApiClient coinCapApiClient, AssetRepository assetRepository) {
        this.coinCapApiClient = coinCapApiClient;
        this.assetRepository = assetRepository;
        this.executorService = initExecutor();
    }

    private ExecutorService initExecutor() {
        return Executors.newFixedThreadPool(MAX_THREADS);
    }

    /**
     * fetchs the latest price from the coincap api and updated the price of the asset
     */
    @Override
    public List<CompletableFuture<Void>> execute() {
        List<String> assetSymbols = assetRepository.findAllDistinctSymbols();

        if (assetSymbols.isEmpty()) {
            logger.warn("No asset found to update price for");
            return List.of();
        }

        return assetSymbols.stream()
                .map(symbol -> CompletableFuture.runAsync(() -> {
                    logger.info("Fetching latest price for token: {}", symbol);
                    try {
                        BigDecimal latestPrice = coinCapApiClient.fetchAssetPrice(symbol);

                        if (latestPrice != null) {
                            assetRepository.updatePriceBySymbol(symbol, latestPrice);
                            logger.info("Successfully updated price for token: {}", symbol);
                        } else {
                            logger.warn("Unable to find price for asset: {}", symbol);
                        }
                    } catch (CoinCapApiException e) {
                        logger.error("Error fetching price for asset: {}", symbol, e);
                    }
                }, executorService))
                .toList();
    }
}
