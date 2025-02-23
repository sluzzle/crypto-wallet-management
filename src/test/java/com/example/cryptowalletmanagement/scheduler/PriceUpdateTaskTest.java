package com.example.cryptowalletmanagement.scheduler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.repository.interfaces.AssetRepository;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class PriceUpdateTaskTest {

    @InjectMocks
    private PriceUpdateTask priceUpdateTask;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CoinCapApiClient coinCapApiClient;

    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        logger = (Logger) LoggerFactory.getLogger(PriceUpdateTask.class);
        listAppender = new ListAppender<>();
        logger.setLevel(Level.ERROR);
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    void execute_ShouldFetchDistinctSymbolsAndUpdatePrices() throws CoinCapApiException {

        List<String> symbols = Arrays.asList("BTC", "ETH");

        when(assetRepository.findAllDistinctSymbols()).thenReturn(symbols);
        when(coinCapApiClient.fetchAssetPrice("BTC")).thenReturn(BigDecimal.valueOf(50000.0));
        when(coinCapApiClient.fetchAssetPrice("ETH")).thenReturn(BigDecimal.valueOf(4000.0));

        priceUpdateTask.execute();

        verify(assetRepository, times(1)).findAllDistinctSymbols();
        verify(coinCapApiClient, times(1)).fetchAssetPrice("BTC");
        verify(coinCapApiClient, times(1)).fetchAssetPrice("ETH");
        verify(assetRepository, times(1)).updatePriceBySymbol("BTC", BigDecimal.valueOf(50000.0));
        verify(assetRepository, times(1)).updatePriceBySymbol("ETH", BigDecimal.valueOf(4000.0));
    }

    @Test
    void execute_ShouldHandleNullPriceGracefully() throws CoinCapApiException {
        List<String> symbols = Arrays.asList("BTC", "ETH");

        when(assetRepository.findAllDistinctSymbols()).thenReturn(symbols);
        when(coinCapApiClient.fetchAssetPrice("BTC")).thenReturn(BigDecimal.valueOf(50000.0));
        when(coinCapApiClient.fetchAssetPrice("ETH")).thenReturn(null); // Simulate null price for ETH

       List<CompletableFuture<Void>> executions = priceUpdateTask.execute();

       //wait for all of compltable features to finish
        CompletableFuture.allOf(executions.toArray(new CompletableFuture[0])).join();

        verify(assetRepository, times(1)).findAllDistinctSymbols();
        verify(coinCapApiClient, times(1)).fetchAssetPrice("BTC");
        verify(coinCapApiClient, times(1)).fetchAssetPrice("ETH");

        verify(assetRepository, times(1)).updatePriceBySymbol("BTC", BigDecimal.valueOf(50000.0));
        verify(assetRepository, times(0)).updatePriceBySymbol(eq("ETH"), any());

    }

}
