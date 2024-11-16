package com.example.service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import com.example.exception.InsufficientFundsException;
import com.example.exception.UserNotFoundException;
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

    public static void transfer(User sourceUser, Account source, User destinationUser, Account destination, BigDecimal amount) throws UserNotFoundException, InsufficientFundsException {
        if (source == null || destination == null){
            throw new UserNotFoundException("El usuario no se pudo encontrar");
        } else if (!hasEnoughAmount(source, amount) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientFundsException("Transferencia inválida.");
        }
        
        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));
    
        updateBalance(sourceUser, source, source.getBalance());
        updateBalance(destinationUser, destination, destination.getBalance());
    }    

    public static boolean updateBalance(User currentUser, Account account, BigDecimal newBalance) {
        String filePath = UserService.getBasePath() + "\\user_" + currentUser.getUserId() + ".txt";
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) { 
                String[] data = lines.get(i).split("@@");
                if (data[1].equals(account.getAccountNumber())) {
                    data[3] = newBalance.toString();
                    lines.set(i, String.join("@@", data));
                    break;
                }
            }
            Files.write(Paths.get(filePath), lines);
            return true;
        } catch (IOException e) {
            System.err.println("Error al actualizar el balance en el archivo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }    
}