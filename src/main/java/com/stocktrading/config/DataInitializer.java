package com.stocktrading.config;

import com.stocktrading.model.Account;
import com.stocktrading.model.Stock;
import com.stocktrading.repository.AccountRepository;
import com.stocktrading.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${initial.cash.balance}")
    private BigDecimal initialCashBalance;

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            Account account = new Account(initialCashBalance);
            accountRepository.save(account);
            System.out.println("Created default account with balance: $" + initialCashBalance);
        }

        if (stockRepository.count() == 0) {
            stockRepository.save(new Stock("AAPL", "Apple Inc.", new BigDecimal("175.50"), "Technology"));
            stockRepository.save(new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("142.30"), "Technology"));
            stockRepository.save(new Stock("MSFT", "Microsoft Corporation", new BigDecimal("378.90"), "Technology"));
            stockRepository.save(new Stock("AMZN", "Amazon.com Inc.", new BigDecimal("155.20"), "Consumer Cyclical"));
            stockRepository.save(new Stock("TSLA", "Tesla Inc.", new BigDecimal("242.80"), "Automotive"));
            stockRepository.save(new Stock("META", "Meta Platforms Inc.", new BigDecimal("485.60"), "Technology"));
            stockRepository.save(new Stock("NVDA", "NVIDIA Corporation", new BigDecimal("495.22"), "Technology"));
            stockRepository.save(new Stock("JPM", "JPMorgan Chase & Co.", new BigDecimal("189.45"), "Financial"));
            stockRepository.save(new Stock("V", "Visa Inc.", new BigDecimal("267.80"), "Financial"));
            stockRepository.save(new Stock("WMT", "Walmart Inc.", new BigDecimal("68.90"), "Consumer Defensive"));
            
            System.out.println("Initialized database with 10 sample stocks");
        }
    }
}
