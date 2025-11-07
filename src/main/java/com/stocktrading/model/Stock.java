package com.stocktrading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    private String companyName;
    
    @Column(nullable = false)
    private BigDecimal currentPrice;
    
    private BigDecimal previousClose;
    
    private BigDecimal changePercent;
    
    private String sector;
    
    private LocalDateTime lastUpdated;
    
    public Stock(String symbol, String companyName, BigDecimal currentPrice, String sector) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.sector = sector;
        this.lastUpdated = LocalDateTime.now();
    }
}
