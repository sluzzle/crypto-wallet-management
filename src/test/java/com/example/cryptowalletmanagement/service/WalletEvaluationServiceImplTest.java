package com.example.cryptowalletmanagement.service;

import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.service.coincap.CoinCapApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class WalletEvaluationServiceImplTest {

    @InjectMocks
    private WalletEvaluationServiceImpl walletEvaluationService;

    @Mock
    private CoinCapApiClient coinCapApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void evaluateWallet_ShouldReturnCorrectEvaluation() throws CoinCapApiException {
        WalletEvaluationInputDTO walletInput = new WalletEvaluationInputDTO(
                Arrays.asList(
                        new WalletEvaluationInputDTO.AssetInputDTO("BTC", BigDecimal.valueOf(0.5), BigDecimal.valueOf(35000.0)),
                        new WalletEvaluationInputDTO.AssetInputDTO("ETH", BigDecimal.valueOf(4.25), BigDecimal.valueOf(15310.71))
                )
        );

        LocalDate date = LocalDate.parse("03/01/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        when(coinCapApiClient.fetchAssetPrice("BTC", date)).thenReturn(BigDecimal.valueOf(94749.27));
        when(coinCapApiClient.fetchAssetPrice("ETH", date)).thenReturn(BigDecimal.valueOf(3699.47));

        double expectedTotal = 63090.0;
        String expectedBestAsset = "BTC";
        double expectedBestPerformance = 35.36;
        String expectedWorstAsset = "ETH";
        double expectedWorstPerformance = 2.677;

        WalletEvaluationOutputDTO result = walletEvaluationService.evaluateWallet(walletInput, date);

        assertEquals(expectedTotal, result.total().doubleValue(), 0.01);
        assertEquals(expectedBestAsset, result.bestAsset());
        assertEquals(expectedBestPerformance, result.bestPerformance());
        assertEquals(expectedWorstAsset, result.worstAsset());
        assertEquals(expectedWorstPerformance, result.worstPerformance());
    }
}