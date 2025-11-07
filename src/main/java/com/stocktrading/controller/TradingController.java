package com.stocktrading.controller;

import com.stocktrading.model.Stock;
import com.stocktrading.service.StockService;
import com.stocktrading.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/trading")
public class TradingController {

    @Autowired
    private StockService stockService;

    @Autowired
    private TradingService tradingService;

    @GetMapping
    public String trading(Model model) {
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", tradingService.getAccount());
        return "trading";
    }

    @PostMapping("/buy")
    public String buyStock(@RequestParam String symbol, 
                          @RequestParam Integer quantity,
                          RedirectAttributes redirectAttributes) {
        String result = tradingService.buyStock(symbol, quantity);
        
        if ("Success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully purchased " + quantity + " shares of " + symbol);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        }
        
        return "redirect:/transactions";
    }

    @PostMapping("/sell")
    public String sellStock(@RequestParam String symbol, 
                           @RequestParam Integer quantity,
                           RedirectAttributes redirectAttributes) {
        String result = tradingService.sellStock(symbol, quantity);
        
        if ("Success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully sold " + quantity + " shares of " + symbol);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        }
        
        return "redirect:/transactions";
    }
}
