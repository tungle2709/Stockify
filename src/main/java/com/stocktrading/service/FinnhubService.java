package com.stocktrading.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FinnhubService {

    @Value("${finnhub.api.key}")
    private String apiKey;

    @Value("${finnhub.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getCurrentPrice(String symbol) {
        try {
            String url = apiUrl + "/quote?symbol=" + symbol + "&token=" + apiKey;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("c")) {
                return new BigDecimal(response.get("c").toString());
            }
        } catch (Exception e) {
            System.err.println("Error fetching price for " + symbol + ": " + e.getMessage());
        }
        return null;
    }

    public Map<String, BigDecimal> getStockQuote(String symbol) {
        try {
            String url = apiUrl + "/quote?symbol=" + symbol + "&token=" + apiKey;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null) {
                Map<String, BigDecimal> quote = new java.util.HashMap<>();
                if (response.containsKey("c")) {
                    quote.put("current", new BigDecimal(response.get("c").toString()));
                }
                if (response.containsKey("pc")) {
                    quote.put("previousClose", new BigDecimal(response.get("pc").toString()));
                }
                return quote;
            }
        } catch (Exception e) {
            System.err.println("Error fetching quote for " + symbol + ": " + e.getMessage());
        }
        return null;
    }
}
