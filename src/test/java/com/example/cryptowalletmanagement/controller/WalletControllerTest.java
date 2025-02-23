package com.example.cryptowalletmanagement.controller;

import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.ControllerExceptionHandler;
import com.example.cryptowalletmanagement.exception.WalletException;
import com.example.cryptowalletmanagement.service.AssetService;
import com.example.cryptowalletmanagement.service.WalletEvaluationService;
import com.example.cryptowalletmanagement.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;
    @Mock
    private AssetService assetService;
    @Mock
    private WalletEvaluationService walletEvaluationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new WalletController(walletService, assetService, walletEvaluationService))
                .setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    @Test
    void createWallet_ShouldCreateWalletSuccessfully() throws Exception {

        WalletDTO walletDTO = new WalletDTO(null, "test@test.com", UUID.randomUUID().toString());

        when(walletService.createWallet(walletDTO.email())).thenReturn(walletDTO);
        final ResultActions resultActions = this.mockMvc.perform(post("/api/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", walletDTO.email()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.token").value(walletDTO.token()));
    }

    @Test
    void createWallet_ShouldThrowExceptionEmailExists() throws Exception {
        String email = "test@test.com";
        String expectedError = "Wallet already exists for this email.";

        when(walletService.createWallet(email)).thenThrow(new WalletException(expectedError));
        final ResultActions resultActions = this.mockMvc.perform(post("/api/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", email));

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedError));
    }

    @Test
    void getWallet_ShouldReturnWalletView() throws Exception {
        WalletDTO wallet = new WalletDTO(1L, "test@example.com", "token");
        when(walletService.getWalletByToken(wallet.token())).thenReturn(wallet);

        AssetDTO asset1 = new AssetDTO(1L, wallet, "BTC", BigDecimal.valueOf(10000), BigDecimal.valueOf(0.1));
        AssetDTO asset2 = new AssetDTO(2L, wallet, "ETH", BigDecimal.valueOf(2000), BigDecimal.valueOf(0.5));
        List<AssetDTO> mockAssets = Arrays.asList(asset1, asset2);
        when(assetService.getAllAssets(wallet.token())).thenReturn(mockAssets);

        BigDecimal totalValue = mockAssets.stream()
                .map(asset -> asset.price().multiply(asset.quantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        mockMvc.perform(get("/api/wallet/{token}", wallet.token())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.id()))
                .andExpect(jsonPath("$.total").value(totalValue.doubleValue()))
                .andExpect(jsonPath("$.assets[0].symbol").value(asset1.symbol()))
                .andExpect(jsonPath("$.assets[0].price").value(asset1.price().doubleValue()))
                .andExpect(jsonPath("$.assets[0].quantity").value(asset1.quantity().doubleValue()))
                .andExpect(jsonPath("$.assets[1].symbol").value(asset2.symbol()))
                .andExpect(jsonPath("$.assets[1].price").value(asset2.price().doubleValue()))
                .andExpect(jsonPath("$.assets[1].quantity").value(asset2.quantity().doubleValue()));

    }

    @Test
    void evaluateWallet_ShouldEvaluateWalletSuccessfully() throws Exception {

        WalletEvaluationOutputDTO walletEvaluation = new WalletEvaluationOutputDTO(BigDecimal.valueOf(65160), "BTC", -83.35, "ETH", -99.88);

        when(walletEvaluationService.evaluateWallet(any(), any())).thenReturn(walletEvaluation);
        final ResultActions resultActions = this.mockMvc.perform(post("/api/wallet/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .param("date", "07/01/2025")
                .content("{\"assets\": [{\"symbol\": \"BTC\", \"quantity\": 0.5, \"value\": 300000.00}, {\"symbol\": \"ETH\", \"quantity\": 4.25, \"value\": 13000010.71}]}"));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.total").value(65160))
                .andExpect(jsonPath("$.bestAsset").value("BTC"))
                .andExpect(jsonPath("$.bestPerformance").value(-83.35))
                .andExpect(jsonPath("$.worstAsset").value("ETH"))
                .andExpect(jsonPath("$.worstPerformance").value(-99.88));

    }
}
