package com.stocktrading.controller.api;

import com.stocktrading.model.Account;
import com.stocktrading.model.Portfolio;
import com.stocktrading.service.PortfolioService;
import com.stocktrading.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioApiController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TradingService tradingService;

    @GetMapping
    public ResponseEntity<List<Portfolio>> getPortfolio() {
        return ResponseEntity.ok(portfolioService.getAllPortfolioItems());
    }

    @GetMapping("/account")
    public ResponseEntity<Account> getAccount() {
        portfolioService.updateAccountMetrics();
        return ResponseEntity.ok(tradingService.getAccount());
    }
}
