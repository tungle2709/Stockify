package com.stocktrading.controller.api;

import com.stocktrading.model.Transaction;
import com.stocktrading.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionApiController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAllByOrderByTransactionDateDesc());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<List<Transaction>> getTransactionsBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(transactionRepository.findByStockSymbolOrderByTransactionDateDesc(symbol));
    }
}
