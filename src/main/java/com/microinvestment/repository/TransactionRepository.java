package com.microinvestment.repository;

import com.microinvestment.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    
    List<Transaction> findByUserIdAndCategoryOrderByTransactionDateDesc(Long userId, String category);
    
    List<Transaction> findByUserId(Long userId);
} 