package com.stocktrading.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private BigDecimal cashBalance;
    
    private BigDecimal totalInvested;
    
    private BigDecimal currentPortfolioValue;
    
    private BigDecimal totalGainLoss;
    
    private BigDecimal gainLossPercentage;
    
    public Account() {}
    
    public Account(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
        this.totalInvested = BigDecimal.ZERO;
        this.currentPortfolioValue = BigDecimal.ZERO;
        this.totalGainLoss = BigDecimal.ZERO;
        this.gainLossPercentage = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getCashBalance() { return cashBalance; }
    public void setCashBalance(BigDecimal cashBalance) { this.cashBalance = cashBalance; }
    public BigDecimal getTotalInvested() { return totalInvested; }
    public void setTotalInvested(BigDecimal totalInvested) { this.totalInvested = totalInvested; }
    public BigDecimal getCurrentPortfolioValue() { return currentPortfolioValue; }
    public void setCurrentPortfolioValue(BigDecimal currentPortfolioValue) { this.currentPortfolioValue = currentPortfolioValue; }
    public BigDecimal getTotalGainLoss() { return totalGainLoss; }
    public void setTotalGainLoss(BigDecimal totalGainLoss) { this.totalGainLoss = totalGainLoss; }
    public BigDecimal getGainLossPercentage() { return gainLossPercentage; }
    public void setGainLossPercentage(BigDecimal gainLossPercentage) { this.gainLossPercentage = gainLossPercentage; }
}
