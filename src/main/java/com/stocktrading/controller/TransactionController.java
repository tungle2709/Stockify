package com.stocktrading.controller;

import com.stocktrading.model.Transaction;
import com.stocktrading.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public String transactions(Model model) {
        List<Transaction> transactions = transactionRepository.findAllByOrderByTransactionDateDesc();
        model.addAttribute("transactions", transactions);
        return "transactions";
    }
}
