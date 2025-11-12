package com.stocktrading.service;

import com.stocktrading.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarketDataService {

    @Autowired
    private StockService stockService;

    @Autowired
    private FinnhubService finnhubService;

    public Map<String, Object> getMarketSummary() {
        Map<String, Object> summary = new HashMap<>();
        List<Stock> stocks = stockService.getAllStocks();
        
        BigDecimal totalMarketCap = BigDecimal.ZERO;
        BigDecimal averageChange = BigDecimal.ZERO;
        int gainers = 0;
        int losers = 0;
        int unchanged = 0;
        
        for (Stock stock : stocks) {
            if (stock.getChangePercent() != null) {
                averageChange = averageChange.add(stock.getChangePercent());
                
                if (stock.getChangePercent().compareTo(BigDecimal.ZERO) > 0) {
                    gainers++;
                } else if (stock.getChangePercent().compareTo(BigDecimal.ZERO) < 0) {
                    losers++;
                } else {
                    unchanged++;
                }
            }
            
            // Estimate market cap (simplified calculation)
            BigDecimal estimatedShares = new BigDecimal("1000000000"); // 1B shares estimate
            totalMarketCap = totalMarketCap.add(stock.getCurrentPrice().multiply(estimatedShares));
        }
        
        if (!stocks.isEmpty()) {
            averageChange = averageChange.divide(new BigDecimal(stocks.size()), 4, RoundingMode.HALF_UP);
        }
        
        summary.put("totalStocks", stocks.size());
        summary.put("gainers", gainers);
        summary.put("losers", losers);
        summary.put("unchanged", unchanged);
        summary.put("averageChange", averageChange);
        summary.put("estimatedMarketCap", totalMarketCap);
        summary.put("lastUpdated", LocalDateTime.now());
        
        return summary;
    }

    public List<Stock> getTopGainers(int limit) {
        List<Stock> stocks = stockService.getAllStocks();
        return stocks.stream()
                .filter(stock -> stock.getChangePercent() != null)
                .sorted((s1, s2) -> s2.getChangePercent().compareTo(s1.getChangePercent()))
                .limit(limit)
                .toList();
    }

    public List<Stock> getTopLosers(int limit) {
        List<Stock> stocks = stockService.getAllStocks();
        return stocks.stream()
                .filter(stock -> stock.getChangePercent() != null)
                .sorted((s1, s2) -> s1.getChangePercent().compareTo(s2.getChangePercent()))
                .limit(limit)
                .toList();
    }

    public List<Stock> getMostActive(int limit) {
        List<Stock> stocks = stockService.getAllStocks();
        return stocks.stream()
                .sorted((s1, s2) -> s2.getCurrentPrice().compareTo(s1.getCurrentPrice()))
                .limit(limit)
                .toList();
    }

    public Map<String, Object> getSectorAnalysis() {
        Map<String, Object> sectorData = new HashMap<>();
        List<Stock> stocks = stockService.getAllStocks();
        Map<String, List<Stock>> sectorGroups = new HashMap<>();
        
        // Group stocks by sector
        for (Stock stock : stocks) {
            String sector = stock.getSector() != null ? stock.getSector() : "Unknown";
            sectorGroups.computeIfAbsent(sector, k -> new ArrayList<>()).add(stock);
        }
        
        Map<String, Map<String, Object>> sectorMetrics = new HashMap<>();
        
        for (Map.Entry<String, List<Stock>> entry : sectorGroups.entrySet()) {
            String sector = entry.getKey();
            List<Stock> sectorStocks = entry.getValue();
            
            BigDecimal totalValue = BigDecimal.ZERO;
            BigDecimal totalChange = BigDecimal.ZERO;
            int count = 0;
            
            for (Stock stock : sectorStocks) {
                totalValue = totalValue.add(stock.getCurrentPrice());
                if (stock.getChangePercent() != null) {
                    totalChange = totalChange.add(stock.getChangePercent());
                    count++;
                }
            }
            
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("stockCount", sectorStocks.size());
            metrics.put("averagePrice", sectorStocks.isEmpty() ? BigDecimal.ZERO : 
                totalValue.divide(new BigDecimal(sectorStocks.size()), 2, RoundingMode.HALF_UP));
            metrics.put("averageChange", count == 0 ? BigDecimal.ZERO : 
                totalChange.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP));
            
            sectorMetrics.put(sector, metrics);
        }
        
        sectorData.put("sectorMetrics", sectorMetrics);
        sectorData.put("totalSectors", sectorGroups.size());
        
        return sectorData;
    }

    public Map<String, Object> getMarketTrends() {
        Map<String, Object> trends = new HashMap<>();
        List<Stock> stocks = stockService.getAllStocks();
        
        // Calculate momentum indicators
        BigDecimal bullishMomentum = BigDecimal.ZERO;
        BigDecimal bearishMomentum = BigDecimal.ZERO;
        
        for (Stock stock : stocks) {
            if (stock.getChangePercent() != null) {
                if (stock.getChangePercent().compareTo(BigDecimal.ZERO) > 0) {
                    bullishMomentum = bullishMomentum.add(stock.getChangePercent());
                } else {
                    bearishMomentum = bearishMomentum.add(stock.getChangePercent().abs());
                }
            }
        }
        
        String marketSentiment = "NEUTRAL";
        if (bullishMomentum.compareTo(bearishMomentum) > 0) {
            marketSentiment = "BULLISH";
        } else if (bearishMomentum.compareTo(bullishMomentum) > 0) {
            marketSentiment = "BEARISH";
        }
        
        trends.put("marketSentiment", marketSentiment);
        trends.put("bullishMomentum", bullishMomentum);
        trends.put("bearishMomentum", bearishMomentum);
        trends.put("volatilityIndex", calculateVolatilityIndex(stocks));
        
        return trends;
    }

    private BigDecimal calculateVolatilityIndex(List<Stock> stocks) {
        BigDecimal volatilitySum = BigDecimal.ZERO;
        int count = 0;
        
        for (Stock stock : stocks) {
            if (stock.getChangePercent() != null) {
                volatilitySum = volatilitySum.add(stock.getChangePercent().abs());
                count++;
            }
        }
        
        return count > 0 ? volatilitySum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    public boolean isMarketOpen() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int dayOfWeek = now.getDayOfWeek().getValue();
        
        // Simplified market hours: Monday-Friday, 9:30 AM - 4:00 PM EST
        return dayOfWeek >= 1 && dayOfWeek <= 5 && hour >= 9 && hour < 16;
    }

    public Map<String, Object> getMarketStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("isOpen", isMarketOpen());
        status.put("timestamp", LocalDateTime.now());
        status.put("timezone", "EST");
        
        if (isMarketOpen()) {
            status.put("status", "OPEN");
            status.put("nextClose", "4:00 PM EST");
        } else {
            status.put("status", "CLOSED");
            status.put("nextOpen", "9:30 AM EST");
        }
        
        return status;
    }
}
