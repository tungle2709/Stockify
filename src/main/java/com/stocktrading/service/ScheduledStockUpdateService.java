package com.stocktrading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledStockUpdateService {

    @Autowired
    private StockService stockService;

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void updateStockPrices() {
        System.out.println("Updating stock prices automatically...");
        stockService.updateAllStockPrices();
    }
}
