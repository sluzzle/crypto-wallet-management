package com.example.cryptowalletmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoWalletManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(CryptoWalletManagementApplication.class, args);
    }
}
