package com.stocktrading.service;

import com.stocktrading.model.*;
import com.stocktrading.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class TradingService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public String buyStock(String symbol, Integer quantity) {
        Optional<Stock> stockOpt = stockRepository.findBySymbol(symbol);
        if (!stockOpt.isPresent()) {
            return "Stock not found";
        }

        Stock stock = stockOpt.get();
        BigDecimal totalCost = stock.getCurrentPrice().multiply(new BigDecimal(quantity));

        Account account = getAccount();
        if (account.getCashBalance().compareTo(totalCost) < 0) {
            return "Insufficient funds";
        }

        account.setCashBalance(account.getCashBalance().subtract(totalCost));
        accountRepository.save(account);

        Optional<Portfolio> portfolioOpt = portfolioRepository.findByStockSymbol(symbol);
        Portfolio portfolio;
        
        if (portfolioOpt.isPresent()) {
            portfolio = portfolioOpt.get();
            BigDecimal totalValue = portfolio.getAveragePurchasePrice()
                    .multiply(new BigDecimal(portfolio.getQuantity()))
                    .add(totalCost);
            portfolio.setQuantity(portfolio.getQuantity() + quantity);
            portfolio.setAveragePurchasePrice(
                    totalValue.divide(new BigDecimal(portfolio.getQuantity()), 2, RoundingMode.HALF_UP)
            );
        } else {
            portfolio = new Portfolio(symbol, quantity, stock.getCurrentPrice());
        }
        portfolioRepository.save(portfolio);

        Transaction transaction = new Transaction(symbol, TransactionType.BUY, 
                quantity, stock.getCurrentPrice(), totalCost);
        transactionRepository.save(transaction);

        return "Success";
    }

    @Transactional
    public String sellStock(String symbol, Integer quantity) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findByStockSymbol(symbol);
        if (!portfolioOpt.isPresent()) {
            return "You don't own this stock";
        }

        Portfolio portfolio = portfolioOpt.get();
        if (portfolio.getQuantity() < quantity) {
            return "Insufficient shares";
        }

        Optional<Stock> stockOpt = stockRepository.findBySymbol(symbol);
        if (!stockOpt.isPresent()) {
            return "Stock not found";
        }

        Stock stock = stockOpt.get();
        BigDecimal totalRevenue = stock.getCurrentPrice().multiply(new BigDecimal(quantity));

        Account account = getAccount();
        account.setCashBalance(account.getCashBalance().add(totalRevenue));
        accountRepository.save(account);

        portfolio.setQuantity(portfolio.getQuantity() - quantity);
        if (portfolio.getQuantity() == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        Transaction transaction = new Transaction(symbol, TransactionType.SELL, 
                quantity, stock.getCurrentPrice(), totalRevenue);
        transactionRepository.save(transaction);

        return "Success";
    }

    public Account getAccount() {
        return accountRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> accountRepository.save(new Account(new BigDecimal("100000"))));
    }
}
