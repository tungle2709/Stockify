package com.stocktrading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    public Account(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
        this.totalInvested = BigDecimal.ZERO;
        this.currentPortfolioValue = BigDecimal.ZERO;
        this.totalGainLoss = BigDecimal.ZERO;
        this.gainLossPercentage = BigDecimal.ZERO;
    }
}
