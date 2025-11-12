package com.stocktrading.config;

import com.stocktrading.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private StockService stockService;

    @Scheduled(fixedRate = 60000) // Every 60 seconds
    public void syncStockPrices() {
        System.out.println("Syncing stock prices with Finnhub API...");
        stockService.updateAllStockPrices();
    }
}
