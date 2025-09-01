package com.microinvestment.service;

import com.microinvestment.dto.TransactionDto;
import com.microinvestment.entity.Transaction;
import com.microinvestment.entity.User;
import com.microinvestment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserService userService;
    
    public Transaction processTransaction(Long userId, TransactionDto transactionDto) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        BigDecimal originalAmount = transactionDto.getAmount();
        BigDecimal chargedAmount = calculateChargedAmount(originalAmount);
        BigDecimal roundUpAmount = chargedAmount.subtract(originalAmount);
        
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setOriginalAmount(originalAmount);
        transaction.setChargedAmount(chargedAmount);
        transaction.setRoundUpAmount(roundUpAmount);
        transaction.setDescription(transactionDto.getDescription());
        transaction.setCategory(transactionDto.getCategory());
        transaction.setTransactionDate(transactionDto.getTransactionDate() != null ? 
            transactionDto.getTransactionDate() : LocalDateTime.now());
        
        // Save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update user's savings balance with round-up amount
        userService.updateSavingsBalance(userId, roundUpAmount);
        
        return savedTransaction;
    }
    
    private BigDecimal calculateChargedAmount(BigDecimal originalAmount) {
        // Round up to the nearest whole number
        return originalAmount.setScale(0, RoundingMode.UP);
    }
    
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }
    
    public BigDecimal getTotalRoundUpAmount(Long userId) {
        return transactionRepository.findByUserId(userId)
            .stream()
            .map(Transaction::getRoundUpAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public List<Transaction> getTransactionsByCategory(Long userId, String category) {
        return transactionRepository.findByUserIdAndCategoryOrderByTransactionDateDesc(userId, category);
    }
} 