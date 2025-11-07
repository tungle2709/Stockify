package com.stocktrading.controller.api;

import com.stocktrading.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/trading")
public class TradingApiController {

    @Autowired
    private TradingService tradingService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@RequestBody Map<String, Object> request) {
        String symbol = (String) request.get("symbol");
        Integer quantity = (Integer) request.get("quantity");
        
        String result = tradingService.buyStock(symbol, quantity);
        
        if ("Success".equals(result)) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Stock purchased successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", result));
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(@RequestBody Map<String, Object> request) {
        String symbol = (String) request.get("symbol");
        Integer quantity = (Integer) request.get("quantity");
        
        String result = tradingService.sellStock(symbol, quantity);
        
        if ("Success".equals(result)) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Stock sold successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", result));
        }
    }
}
