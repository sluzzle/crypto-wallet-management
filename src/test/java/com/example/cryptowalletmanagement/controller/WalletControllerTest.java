package com.example.cryptowalletmanagement.controller;

import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
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

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .content(walletDTO.email()));

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
                .content(email));

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedError));
    }
}
