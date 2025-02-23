package com.example.cryptowalletmanagement.controller;

import com.example.cryptowalletmanagement.controller.request.AssetAddRequest;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AssetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssetService assetService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

        when(assetService.saveAsset(any(), any(), any())).thenReturn(ethAsset);

        AssetAddRequest assetAddRequest = new AssetAddRequest(wallet.token(), ethAsset.symbol(), ethAsset.quantity());

        final ResultActions resultActions = this.mockMvc.perform(post("/api/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetAddRequest)));
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(AssetView.fromAssetDTO(ethAsset))));
    }

    @Test
    void addAsset_ShouldThrowExceptionPriceNotFound() throws Exception {
        AssetAddRequest assetAddRequest = new AssetAddRequest("token", "ETH", null);

        final ResultActions resultActions = this.mockMvc.perform(post("/api/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetAddRequest)));

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsInAnyOrder
                        ("Quantity must not be null")));
    }
}
