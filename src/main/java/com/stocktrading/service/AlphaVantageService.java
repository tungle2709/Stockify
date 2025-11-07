package com.stocktrading.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AlphaVantageService {

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    @Value("${alpha.vantage.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getStockPrice(String symbol) {
        try {
            String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", 
                    apiUrl, symbol, apiKey);
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("Global Quote")) {
                Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
                String priceStr = quote.get("05. price");
                if (priceStr != null && !priceStr.isEmpty()) {
                    return new BigDecimal(priceStr);
                }
            }
            
            return generateMockPrice(symbol);
            
        } catch (Exception e) {
            return generateMockPrice(symbol);
        }
    }

    private BigDecimal generateMockPrice(String symbol) {
        int hash = Math.abs(symbol.hashCode());
        double basePrice = 50 + (hash % 200);
        double variation = (Math.random() - 0.5) * 10;
        return BigDecimal.valueOf(basePrice + variation).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
