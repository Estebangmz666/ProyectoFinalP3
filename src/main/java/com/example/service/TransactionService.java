package com.example.service;

import java.math.BigDecimal;

import com.example.model.Account;
import com.example.model.User;

public class TransactionService {

    public static boolean hasEnoughAmount(Account source, BigDecimal amount) {
        return source != null && source.getBalance().compareTo(amount) >= 0;
    }

    public static boolean isPositiveAmount(BigDecimal amount){
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isAmountZero(BigDecimal amount){
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public static void transfer(Account source, Account destination, BigDecimal amount){
        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));
    }

    public static void updateBalance(User currentUser, BigDecimal amount){
        //TO-DO
    }
}