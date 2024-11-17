package com.example.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.exception.InsufficientFundsException;
import com.example.exception.UserNotFoundException;
import com.example.model.Account;
import com.example.model.Budget;
import com.example.model.Transaction;
import com.example.model.TransactionType;
import com.example.model.User;
import com.example.util.PropertiesLoader;
import com.example.util.SerializeDeserialize;

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
        String filePath = PropertiesLoader.getRutaFromProperties("base_path") + "\\user_" + currentUser.getUserId() + ".txt";
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

    public static List<Transaction> getRecentTransactions(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String userFile = PropertiesLoader.getRutaFromProperties("transaction_base_path") + "User" + userId + "_transactions.txt";
    
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionData = line.split("@@");
                if (transactionData.length == 8) {
                    String transactionId = transactionData[0];
                    String date = transactionData[1];
                    String transactionType = transactionData[2];
                    String amount = transactionData[3];
                    String description = transactionData[4];
                    String sourceAccount = transactionData[5];
                    String destinationAccount = transactionData[6];
                    String category = transactionData[7];
                    Transaction transaction = new Transaction(
                        transactionId,
                        LocalDateTime.parse(date),
                        TransactionType.valueOf(transactionType),
                        new BigDecimal(amount),
                        description,
                        new Account(sourceAccount),
                        new Account(destinationAccount),
                        category
                    );
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }    

    public static void updateUserBudgetsAfterTransaction(int userId, BigDecimal transactionAmount, TransactionType transactionType) {
        List<Budget> budgets = SerializeDeserialize.loadBudgets(userId);
        BigDecimal remainingAmount = transactionAmount;
        for (Budget budget : budgets) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal totalAmount = BigDecimal.valueOf(budget.getTotalAmount());
            BigDecimal spentAmount = BigDecimal.valueOf(budget.getSpentAmount());
            BigDecimal amountToAdjust = remainingAmount.min(totalAmount.subtract(spentAmount));
            if (transactionType == TransactionType.RETIRO || transactionType == TransactionType.TRANSFERENCIA) {
                spentAmount = spentAmount.add(amountToAdjust);
                budget.setSpentAmount(spentAmount.doubleValue());
                remainingAmount = remainingAmount.subtract(amountToAdjust);
                BudgetService.updateBudgetInFile(userId, budget, amountToAdjust);
            }
        }
    }       
}