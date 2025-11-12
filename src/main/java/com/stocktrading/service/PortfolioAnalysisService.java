package com.stocktrading.service;

import com.stocktrading.model.Portfolio;
import com.stocktrading.model.Stock;
import com.stocktrading.model.Transaction;
import com.stocktrading.repository.PortfolioRepository;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PortfolioAnalysisService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Map<String, Object> calculatePortfolioMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        List<Portfolio> portfolios = portfolioRepository.findAll();
        
        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalGainLoss = BigDecimal.ZERO;
        
        for (Portfolio portfolio : portfolios) {
            Optional<Stock> stockOpt = stockRepository.findBySymbol(portfolio.getStockSymbol());
            if (stockOpt.isPresent()) {
                Stock stock = stockOpt.get();
                BigDecimal currentValue = stock.getCurrentPrice().multiply(new BigDecimal(portfolio.getQuantity()));
                BigDecimal costBasis = portfolio.getAveragePurchasePrice().multiply(new BigDecimal(portfolio.getQuantity()));
                
                totalValue = totalValue.add(currentValue);
                totalCost = totalCost.add(costBasis);
                totalGainLoss = totalGainLoss.add(currentValue.subtract(costBasis));
            }
        }
        
        BigDecimal gainLossPercentage = BigDecimal.ZERO;
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            gainLossPercentage = totalGainLoss.divide(totalCost, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }
        
        metrics.put("totalValue", totalValue);
        metrics.put("totalCost", totalCost);
        metrics.put("totalGainLoss", totalGainLoss);
        metrics.put("gainLossPercentage", gainLossPercentage);
        metrics.put("portfolioCount", portfolios.size());
        
        return metrics;
    }

    public Map<String, BigDecimal> calculateStockAllocation() {
        Map<String, BigDecimal> allocation = new HashMap<>();
        List<Portfolio> portfolios = portfolioRepository.findAll();
        BigDecimal totalValue = BigDecimal.ZERO;
        
        Map<String, BigDecimal> stockValues = new HashMap<>();
        
        for (Portfolio portfolio : portfolios) {
            Optional<Stock> stockOpt = stockRepository.findBySymbol(portfolio.getStockSymbol());
            if (stockOpt.isPresent()) {
                Stock stock = stockOpt.get();
                BigDecimal value = stock.getCurrentPrice().multiply(new BigDecimal(portfolio.getQuantity()));
                stockValues.put(portfolio.getStockSymbol(), value);
                totalValue = totalValue.add(value);
            }
        }
        
        for (Map.Entry<String, BigDecimal> entry : stockValues.entrySet()) {
            if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentage = entry.getValue().divide(totalValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                allocation.put(entry.getKey(), percentage);
            }
        }
        
        return allocation;
    }

    public Map<String, Object> calculateRiskMetrics() {
        Map<String, Object> riskMetrics = new HashMap<>();
        List<Portfolio> portfolios = portfolioRepository.findAll();
        
        BigDecimal volatilitySum = BigDecimal.ZERO;
        int count = 0;
        
        for (Portfolio portfolio : portfolios) {
            Optional<Stock> stockOpt = stockRepository.findBySymbol(portfolio.getStockSymbol());
            if (stockOpt.isPresent()) {
                Stock stock = stockOpt.get();
                BigDecimal volatility = calculateStockVolatility(stock.getSymbol());
                volatilitySum = volatilitySum.add(volatility);
                count++;
            }
        }
        
        BigDecimal averageVolatility = count > 0 ? volatilitySum.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        
        riskMetrics.put("averageVolatility", averageVolatility);
        riskMetrics.put("diversificationScore", calculateDiversificationScore());
        riskMetrics.put("riskLevel", determineRiskLevel(averageVolatility));
        
        return riskMetrics;
    }

    private BigDecimal calculateStockVolatility(String symbol) {
        List<Transaction> transactions = transactionRepository.findByStockSymbolOrderByTransactionDateDesc(symbol);
        if (transactions.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal sumSquares = BigDecimal.ZERO;
        int count = Math.min(transactions.size(), 30); // Last 30 transactions
        
        for (int i = 0; i < count; i++) {
            BigDecimal price = transactions.get(i).getPricePerShare();
            sum = sum.add(price);
            sumSquares = sumSquares.add(price.multiply(price));
        }
        
        BigDecimal mean = sum.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP);
        BigDecimal variance = sumSquares.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP).subtract(mean.multiply(mean));
        
        return variance.compareTo(BigDecimal.ZERO) > 0 ? 
            new BigDecimal(Math.sqrt(variance.doubleValue())).setScale(4, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
    }

    private BigDecimal calculateDiversificationScore() {
        Map<String, BigDecimal> allocation = calculateStockAllocation();
        if (allocation.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal herfindahlIndex = BigDecimal.ZERO;
        for (BigDecimal percentage : allocation.values()) {
            BigDecimal normalizedPercentage = percentage.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            herfindahlIndex = herfindahlIndex.add(normalizedPercentage.multiply(normalizedPercentage));
        }
        
        return BigDecimal.ONE.subtract(herfindahlIndex).multiply(new BigDecimal("100"));
    }

    private String determineRiskLevel(BigDecimal volatility) {
        if (volatility.compareTo(new BigDecimal("10")) < 0) {
            return "LOW";
        } else if (volatility.compareTo(new BigDecimal("25")) < 0) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    public Map<String, Object> generatePerformanceReport() {
        Map<String, Object> report = new HashMap<>();
        
        report.put("portfolioMetrics", calculatePortfolioMetrics());
        report.put("stockAllocation", calculateStockAllocation());
        report.put("riskMetrics", calculateRiskMetrics());
        report.put("generatedAt", LocalDateTime.now());
        
        List<Transaction> recentTransactions = transactionRepository.findTop10ByOrderByTransactionDateDesc();
        report.put("recentTransactions", recentTransactions);
        
        return report;
    }
}
