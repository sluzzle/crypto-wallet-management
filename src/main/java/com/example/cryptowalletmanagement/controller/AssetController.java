package com.example.cryptowalletmanagement.controller;

import com.example.cryptowalletmanagement.controller.request.AssetAddRequest;
import com.example.cryptowalletmanagement.controller.views.AssetView;
import com.example.cryptowalletmanagement.dto.asset.AssetDTO;
import com.example.cryptowalletmanagement.exception.AssetException;
import com.example.cryptowalletmanagement.exception.CoinCapApiException;
import com.example.cryptowalletmanagement.exception.WalletException;
import com.example.cryptowalletmanagement.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing crypto assets.
 */
@RestController
@RequestMapping("/api/asset")
@Tag(name = "Asset Management", description = "APIs for managing a crypto asset")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }
    /**
     * add a crypto assets to the given wallet
     * @param asset
     * @return
     * @throws WalletException
     * @throws AssetException
     * @throws CoinCapApiException
     */
    @PostMapping
    @Operation(summary = "Add a crypto asset to a wallet")
    public ResponseEntity<AssetView> addAsset(
             @Valid @RequestBody AssetAddRequest asset) {
        AssetDTO assetDTO = assetService.saveAsset(asset.walletToken(), asset.symbol(), asset.quantity());
        return ResponseEntity.ok(AssetView.fromAssetDTO(assetDTO));
    }
}
