package com.microinvestment.controller;

import com.microinvestment.dto.TransactionDto;
import com.microinvestment.entity.Transaction;
import com.microinvestment.entity.User;
import com.microinvestment.service.TransactionService;
import com.microinvestment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            Transaction transaction = transactionService.processTransaction(user.getId(), transactionDto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Transaction processed successfully");
            response.put("transactionId", transaction.getId());
            response.put("originalAmount", transaction.getOriginalAmount());
            response.put("chargedAmount", transaction.getChargedAmount());
            response.put("roundUpAmount", transaction.getRoundUpAmount());
            response.put("savingsBalance", user.getSavingsBalance());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getUserTransactions() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<Transaction> transactions = transactionService.getUserTransactions(user.getId());
            BigDecimal totalRoundUp = transactionService.getTotalRoundUpAmount(user.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("transactions", transactions);
            response.put("totalRoundUp", totalRoundUp);
            response.put("savingsBalance", user.getSavingsBalance());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getTransactionsByCategory(@PathVariable String category) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<Transaction> transactions = transactionService.getTransactionsByCategory(user.getId(), category);
            
            Map<String, Object> response = new HashMap<>();
            response.put("transactions", transactions);
            response.put("category", category);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 