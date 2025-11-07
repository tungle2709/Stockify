package com.stocktrading.controller;

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
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TradingService tradingService;

    @GetMapping
    public String portfolio(Model model) {
        portfolioService.updateAccountMetrics();
        List<Portfolio> portfolioItems = portfolioService.getAllPortfolioItems();
        model.addAttribute("portfolioItems", portfolioItems);
        model.addAttribute("account", tradingService.getAccount());
        return "portfolio";
    }
}
