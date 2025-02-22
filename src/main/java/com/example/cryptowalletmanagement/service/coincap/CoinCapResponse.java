package com.example.cryptowalletmanagement.service.coincap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinCapResponse {
    private String error;
    private long timestamp;
    private Map<String, String> data;
}