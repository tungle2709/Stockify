package com.stocktrading.controller;

import com.stocktrading.model.Account;
import com.stocktrading.model.Portfolio;
import com.stocktrading.service.PortfolioService;
import com.stocktrading.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profit-loss")
public class ProfitLossController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TradingService tradingService;

    @GetMapping
    public String profitLoss(Model model) {
        portfolioService.updateAccountMetrics();
        Account account = tradingService.getAccount();
        List<Portfolio> portfolioItems = portfolioService.getAllPortfolioItems();
        
        model.addAttribute("account", account);
        model.addAttribute("portfolioItems", portfolioItems);
        
        return "profit-loss";
    }
}
