package com.microinvestment.service;

import com.microinvestment.entity.User;
import com.microinvestment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class InvestmentSchedulerService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MockInvestmentPlatformService mockInvestmentService;
    
    // Process investments every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void processDailyInvestments() {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            if (user.getSavingsBalance().compareTo(BigDecimal.ZERO) > 0) {
                processUserInvestment(user);
            }
        }
    }
    
    // Process investments every hour for demonstration
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void processHourlyInvestments() {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            if (user.getSavingsBalance().compareTo(new BigDecimal("10")) >= 0) { // Minimum ₹10 to invest
                processUserInvestment(user);
            }
        }
    }
    
    private void processUserInvestment(User user) {
        try {
            // Calculate investment amount (e.g., 80% of savings)
            BigDecimal investmentAmount = user.getSavingsBalance()
                .multiply(new BigDecimal("0.8"))
                .setScale(2, RoundingMode.HALF_UP);
            
            if (investmentAmount.compareTo(BigDecimal.ZERO) > 0) {
                // Process investment through mock platform
                boolean investmentSuccess = mockInvestmentService.processInvestment(
                    user.getId(), investmentAmount);
                
                if (investmentSuccess) {
                    // Update user balances
                    user.setSavingsBalance(user.getSavingsBalance().subtract(investmentAmount));
                    user.setInvestmentBalance(user.getInvestmentBalance().add(investmentAmount));
                    userRepository.save(user);
                    
                    System.out.println("Investment processed for user " + user.getUsername() + 
                        ": ₹" + investmentAmount);
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing investment for user " + user.getUsername() + ": " + e.getMessage());
        }
    }
} 