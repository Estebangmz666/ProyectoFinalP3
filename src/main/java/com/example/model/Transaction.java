package com.example.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable{
    private String transactionId;
    private LocalDate date;
    private TransactionType transactionType;
    private double amount;
    private String description;
    private Account sourceAccount;
    private Account destinationAccount;
    private String category;
    
    public Transaction(String transactionId, LocalDate date, TransactionType transactionType, double amount,
            String description, Account sourceAccount, Account destinationAccount, String category) {
        this.transactionId = transactionId;
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.category = category;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
