package com.stocktrading.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
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
    
    public Stock() {}
    
    public Stock(String symbol, String companyName, BigDecimal currentPrice, String sector) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.sector = sector;
        this.lastUpdated = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public BigDecimal getPreviousClose() { return previousClose; }
    public void setPreviousClose(BigDecimal previousClose) { this.previousClose = previousClose; }
    public BigDecimal getChangePercent() { return changePercent; }
    public void setChangePercent(BigDecimal changePercent) { this.changePercent = changePercent; }
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public BigDecimal getDollarChange() {
        if (currentPrice != null && previousClose != null) {
            return currentPrice.subtract(previousClose);
        }
        return BigDecimal.ZERO;
    }
}
