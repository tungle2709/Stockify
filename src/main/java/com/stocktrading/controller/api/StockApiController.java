package com.stocktrading.controller.api;

import com.stocktrading.model.Stock;
import com.stocktrading.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
public class StockApiController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable String symbol) {
        Optional<Stock> stock = stockService.getStockBySymbol(symbol);
        if (stock.isPresent()) {
            // Update price for real-time data
            stockService.updateStockPrice(symbol);
            return ResponseEntity.ok(stockService.getStockBySymbol(symbol).get());
        } else {
            // Try to add new stock
            Stock newStock = stockService.addStock(symbol);
            if (newStock != null) {
                return ResponseEntity.ok(newStock);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshStockPrices() {
        stockService.updateAllStockPrices();
        return ResponseEntity.ok("Stock prices updated successfully");
    }
}
