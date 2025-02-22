package com.example.cryptowalletmanagement.scheduler;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.repository.interfaces.AssetRepository;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class PriceUpdateTaskTest {

    @InjectMocks
    private PriceUpdateTask priceUpdateTask;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CoinCapApiClient coinCapApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateTokenPrices_ShouldFetchDistinctSymbolsAndUpdatePrices() throws CoinCapApiException {

        List<String> symbols = Arrays.asList("BTC", "ETH");

        when(assetRepository.findAllDistinctSymbols()).thenReturn(symbols);
        when(coinCapApiClient.fetchCoinPrice("BTC")).thenReturn(BigDecimal.valueOf(50000.0));
        when(coinCapApiClient.fetchCoinPrice("ETH")).thenReturn(BigDecimal.valueOf(4000.0));

        priceUpdateTask.execute();

        verify(assetRepository, times(1)).findAllDistinctSymbols();
        verify(coinCapApiClient, times(1)).fetchCoinPrice("BTC");
        verify(coinCapApiClient, times(1)).fetchCoinPrice("ETH");
        verify(assetRepository, times(1)).updatePriceBySymbol("BTC", BigDecimal.valueOf(50000.0));
        verify(assetRepository, times(1)).updatePriceBySymbol("ETH", BigDecimal.valueOf(4000.0));
    }

    @Test
    void updateTokenPrices_ShouldHandleNullPriceGracefully() throws CoinCapApiException {
        List<String> symbols = Arrays.asList("BTC", "ETH");

        when(assetRepository.findAllDistinctSymbols()).thenReturn(symbols);
        when(coinCapApiClient.fetchCoinPrice("BTC")).thenReturn(BigDecimal.valueOf(50000.0));
        when(coinCapApiClient.fetchCoinPrice("ETH")).thenReturn(null); // Simulate null price for ETH

        priceUpdateTask.execute();

        verify(assetRepository, times(1)).findAllDistinctSymbols();
        verify(coinCapApiClient, times(1)).fetchCoinPrice("BTC");
        verify(coinCapApiClient, times(1)).fetchCoinPrice("ETH");

        verify(assetRepository, times(1)).updatePriceBySymbol("BTC", BigDecimal.valueOf(50000.0));
        verify(assetRepository, times(0)).updatePriceBySymbol(eq("ETH"), any());

    }

    @Test
    void updateTokenPrices_ShouldHandleExceptionsFromApiClient() throws CoinCapApiException {
        List<String> symbols = Arrays.asList("BTC", "ETH");

        when(assetRepository.findAllDistinctSymbols()).thenReturn(symbols);
        when(coinCapApiClient.fetchCoinPrice("BTC")).thenReturn(BigDecimal.valueOf(50000.0));
        when(coinCapApiClient.fetchCoinPrice("ETH")).thenThrow(new RuntimeException("API failure"));

        priceUpdateTask.execute();

        verify(assetRepository, times(1)).findAllDistinctSymbols();
        verify(coinCapApiClient, times(1)).fetchCoinPrice("BTC");
        verify(coinCapApiClient, times(1)).fetchCoinPrice("ETH");

        verify(assetRepository, times(1)).updatePriceBySymbol("BTC", BigDecimal.valueOf(50000.0));
        verify(assetRepository, times(0)).updatePriceBySymbol(eq("ETH"), any());
    }
}
