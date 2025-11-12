package com.stocktrading.service;

import com.stocktrading.model.Stock;
import com.stocktrading.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private FinnhubService finnhubService;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public void updateStockPrice(String symbol) {
        Optional<Stock> stockOpt = stockRepository.findBySymbol(symbol);
        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            BigDecimal newPrice = finnhubService.getCurrentPrice(symbol);
            if (newPrice != null) {
                stock.setPreviousClose(stock.getCurrentPrice());
                stock.setCurrentPrice(newPrice);
                if (stock.getPreviousClose() != null && stock.getPreviousClose().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal change = newPrice.subtract(stock.getPreviousClose());
                    BigDecimal changePercent = change.divide(stock.getPreviousClose(), 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                    stock.setChangePercent(changePercent);
                }
                stock.setLastUpdated(LocalDateTime.now());
                stockRepository.save(stock);
            }
        }
    }

    public Stock addStock(String symbol) {
        // Check if stock already exists
        Optional<Stock> existingStock = stockRepository.findBySymbol(symbol);
        if (existingStock.isPresent()) {
            updateStockPrice(symbol);
            return existingStock.get();
        }

        // Create new stock with real-time data
        BigDecimal currentPrice = finnhubService.getCurrentPrice(symbol);
        if (currentPrice == null) {
            return null; // Stock not found
        }

        Stock newStock = new Stock();
        newStock.setSymbol(symbol);
        newStock.setCompanyName(symbol + " Inc."); // Simplified company name
        newStock.setCurrentPrice(currentPrice);
        newStock.setPreviousClose(currentPrice);
        newStock.setChangePercent(BigDecimal.ZERO);
        newStock.setSector("Technology");
        newStock.setLastUpdated(LocalDateTime.now());

        return stockRepository.save(newStock);
    }

    public void updateAllStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            updateStockPrice(stock.getSymbol());
        }
    }
}
