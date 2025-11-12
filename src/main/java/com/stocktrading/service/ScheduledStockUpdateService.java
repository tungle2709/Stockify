package com.stocktrading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledStockUpdateService {

    @Autowired
    private StockService stockService;

    // Temporarily disabled to reduce memory usage
    // @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void updateStockPrices() {
        System.out.println("Updating stock prices automatically...");
        stockService.updateAllStockPrices();
    }
}
