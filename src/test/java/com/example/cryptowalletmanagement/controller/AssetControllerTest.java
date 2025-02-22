package com.example.cryptowalletmanagement.controller;

import com.example.cryptowalletmanagement.controller.views.AssetView;
import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
import com.example.cryptowalletmanagement.exception.ControllerExceptionHandler;
import com.example.cryptowalletmanagement.service.AssetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AssetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssetService assetService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AssetController(assetService))
                .setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    @Test
    void addAsset_shouldAddAssetToWallet() throws Exception {
        WalletDTO wallet = new WalletDTO(1L, "test@test.com", "token");
        AssetDTO ethAsset = new AssetDTO(1L, wallet, "ETH", BigDecimal.valueOf(3), BigDecimal.valueOf(3000));
        String ethAssetJson = "{\"token\":\"token\",\"symbol\":\"ETH\",\"quantity\":3,\"price\":3000.0}";
        when(assetService.saveAsset(any(), any(), any(), any())).thenReturn(ethAsset);
        String assetViewJson = new ObjectMapper().writeValueAsString(AssetView.fromAssetDTO(ethAsset));
        final ResultActions resultActions = this.mockMvc.perform(post("/api/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ethAssetJson));
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(assetViewJson));
    }

    @Test
    void addAsset_ShouldThrowExceptionPriceNotFound() throws Exception {
        WalletDTO wallet = new WalletDTO(1L, "test@test.com", "token");
        AssetDTO ethAsset = new AssetDTO(1L, wallet, "ETH", BigDecimal.valueOf(3), BigDecimal.valueOf(3000));
        String ethAssetJson = "{\"token\":\"token\",\"symbol\":\"ETH\",\"quantity\":3,\"price\":3000.0}";
        when(assetService.saveAsset(any(), any(), any(), any())).thenReturn(ethAsset);
        String assetViewJson = new ObjectMapper().writeValueAsString(AssetView.fromAssetDTO(ethAsset));
        final ResultActions resultActions = this.mockMvc.perform(post("/api/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ethAssetJson));
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(assetViewJson));
    }
}
