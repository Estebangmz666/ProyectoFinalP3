package com.example.service;

import java.math.BigDecimal;

import com.example.exception.*;

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
    
    public static void transfer(Account source, Account destination, BigDecimal amount) throws InsufficientFundsException {
        if (!hasEnoughAmount(source, amount)) {
            throw new InsufficientFundsException("Fondos insuficientes para realizar la transferencia.");
        }
        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));
    }

    public static void updateBalance(User currentUser, BigDecimal amount){
        //TO-DO
    }
}