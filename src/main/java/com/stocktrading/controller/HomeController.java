package com.stocktrading.controller;

import com.stocktrading.model.Stock;
import com.stocktrading.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private StockService stockService;

    @GetMapping("/")
    public String home(Model model) {
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("stocks", stocks);
        return "stocks";
    }

    @GetMapping("/stocks")
    public String stocks(Model model) {
        stockService.updateAllStockPrices();
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("stocks", stocks);
        return "stocks";
    }
}
