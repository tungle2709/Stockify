package com.stocktrading.service;

import com.stocktrading.model.Account;
import com.stocktrading.model.Portfolio;
import com.stocktrading.model.Stock;
import com.stocktrading.repository.AccountRepository;
import com.stocktrading.repository.PortfolioRepository;
import com.stocktrading.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Portfolio> getAllPortfolioItems() {
        List<Portfolio> portfolioItems = portfolioRepository.findAll();
        
        for (Portfolio item : portfolioItems) {
            stockRepository.findBySymbol(item.getStockSymbol()).ifPresent(stock -> {
                BigDecimal currentValue = stock.getCurrentPrice()
                        .multiply(new BigDecimal(item.getQuantity()));
                item.setCurrentValue(currentValue);
                
                BigDecimal invested = item.getAveragePurchasePrice()
                        .multiply(new BigDecimal(item.getQuantity()));
                BigDecimal gainLoss = currentValue.subtract(invested);
                item.setTotalGainLoss(gainLoss);
                
                if (invested.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal gainLossPercent = gainLoss.divide(invested, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                    item.setGainLossPercentage(gainLossPercent);
                }
            });
        }
        
        return portfolioItems;
    }

    public void updateAccountMetrics() {
        List<Portfolio> portfolioItems = getAllPortfolioItems();
        Account account = accountRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> accountRepository.save(new Account(new BigDecimal("100000"))));

        BigDecimal totalPortfolioValue = BigDecimal.ZERO;
        BigDecimal totalInvested = BigDecimal.ZERO;

        for (Portfolio item : portfolioItems) {
            if (item.getCurrentValue() != null) {
                totalPortfolioValue = totalPortfolioValue.add(item.getCurrentValue());
            }
            totalInvested = totalInvested.add(
                    item.getAveragePurchasePrice().multiply(new BigDecimal(item.getQuantity()))
            );
        }

        account.setCurrentPortfolioValue(totalPortfolioValue);
        account.setTotalInvested(totalInvested);

        BigDecimal totalGainLoss = totalPortfolioValue.subtract(totalInvested);
        account.setTotalGainLoss(totalGainLoss);

        if (totalInvested.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal gainLossPercent = totalGainLoss.divide(totalInvested, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            account.setGainLossPercentage(gainLossPercent);
        }

        accountRepository.save(account);
    }
}
