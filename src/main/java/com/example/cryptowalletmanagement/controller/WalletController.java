package com.example.cryptowalletmanagement.controller;

import com.example.cryptowalletmanagement.controller.views.WalletView;
import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletDetailDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationInputDTO;
import com.example.cryptowalletmanagement.dto.wallet.WalletEvaluationOutputDTO;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.exception.WalletException;
import com.example.cryptowalletmanagement.service.AssetService;
import com.example.cryptowalletmanagement.service.WalletEvaluationService;
import com.example.cryptowalletmanagement.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller class for managing a wallet
 */
@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet Management", description = "APIs for managing a crypto wallet")
public class WalletController {

    private final WalletService walletService;
    private final AssetService assetService;
    private final WalletEvaluationService walletEvaluationService;

    public WalletController(WalletService walletService, AssetService assetService, WalletEvaluationService walletEvaluationService) {
        this.walletService = walletService;
        this.assetService = assetService;
        this.walletEvaluationService = walletEvaluationService;
    }

    @PostMapping
    @Operation(summary = "Create a new wallet")
    public ResponseEntity<WalletView> createWallet(@Email @RequestParam String email) {
        return ResponseEntity.ok(WalletView
                .fromWalletDTO(walletService.createWallet(email))
        );
    }

    /**
     * returns a wallet detail with total and all assets and their value
     *
     * @param token
     * @return
     * @throws WalletException
     */
    @GetMapping("/{token}")
    @Operation(summary = "Retreive wallet detail")
    public ResponseEntity<WalletDetailDTO> getWallet(@PathVariable String token) {
        List<AssetDTO> assets = assetService.getAllAssets(token);
        WalletDTO wallet = walletService.getWalletByToken(token);
        BigDecimal totalValue = assets.stream()
                .map(asset -> asset.price().multiply(asset.quantity())).reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok(WalletDetailDTO.of(wallet.id(), totalValue, assets));
    }

    /**
     * Evaluates the given wallet based input and date
     *
     * @param inputDTO
     * @param date
     * @return Json Response
     * @throws CoinCapApiException
     */
    @PostMapping("/evaluate")
    @Operation(summary = "Evaluate a given wallet")
    public ResponseEntity<WalletEvaluationOutputDTO> evaluateWallet(
            @RequestBody WalletEvaluationInputDTO inputDTO,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        WalletEvaluationOutputDTO result = walletEvaluationService.evaluateWallet(inputDTO, date);
        return ResponseEntity.ok(result);
    }


}
