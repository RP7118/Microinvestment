package com.microinvestment.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class MockInvestmentPlatformService {
    
    private final Random random = new Random();
    
    public boolean processInvestment(Long userId, BigDecimal amount) {
        // Simulate investment platform processing
        // In a real scenario, this would call external investment APIs
        
        try {
            // Simulate network delay
            Thread.sleep(random.nextInt(1000) + 500);
            
            // Simulate success/failure (90% success rate for demo)
            boolean success = random.nextDouble() < 0.9;
            
            if (success) {
                System.out.println("Mock investment platform: Successfully invested ₹" + 
                    amount + " for user " + userId);
            } else {
                System.out.println("Mock investment platform: Failed to invest ₹" + 
                    amount + " for user " + userId);
            }
            
            return success;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    public BigDecimal getInvestmentReturns(Long userId) {
        // Simulate investment returns (random 0.5% to 2% return)
        double returnRate = 0.005 + (random.nextDouble() * 0.015);
        return new BigDecimal(returnRate).setScale(4, BigDecimal.ROUND_HALF_UP);
    }
    
    public String getInvestmentStatus(Long userId) {
        String[] statuses = {"ACTIVE", "PENDING", "COMPLETED", "FAILED"};
        return statuses[random.nextInt(statuses.length)];
    }
} 