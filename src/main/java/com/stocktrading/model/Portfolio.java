package com.stocktrading.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "portfolio")
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
    
    public Portfolio() {}
    
    public Portfolio(String stockSymbol, Integer quantity, BigDecimal averagePurchasePrice) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.averagePurchasePrice = averagePurchasePrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStockSymbol() { return stockSymbol; }
    public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getAveragePurchasePrice() { return averagePurchasePrice; }
    public void setAveragePurchasePrice(BigDecimal averagePurchasePrice) { this.averagePurchasePrice = averagePurchasePrice; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public BigDecimal getTotalGainLoss() { return totalGainLoss; }
    public void setTotalGainLoss(BigDecimal totalGainLoss) { this.totalGainLoss = totalGainLoss; }
    public BigDecimal getGainLossPercentage() { return gainLossPercentage; }
    public void setGainLossPercentage(BigDecimal gainLossPercentage) { this.gainLossPercentage = gainLossPercentage; }
}
