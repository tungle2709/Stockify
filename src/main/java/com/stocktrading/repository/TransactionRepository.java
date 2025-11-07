package com.stocktrading.repository;

import com.stocktrading.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStockSymbolOrderByTransactionDateDesc(String stockSymbol);
    List<Transaction> findAllByOrderByTransactionDateDesc();
}
