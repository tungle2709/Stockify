package com.stocktrading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "portfolio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String stockSymbol;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private BigDecimal averagePurchasePrice;
    
    private BigDecimal currentValue;
    
    private BigDecimal totalGainLoss;
    
    private BigDecimal gainLossPercentage;
    
    public Portfolio(String stockSymbol, Integer quantity, BigDecimal averagePurchasePrice) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.averagePurchasePrice = averagePurchasePrice;
    }
}
