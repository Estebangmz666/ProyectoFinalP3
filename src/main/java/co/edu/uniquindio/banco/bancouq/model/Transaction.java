package co.edu.uniquindio.banco.bancouq.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transactionId;
    private LocalDateTime date;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;
    private Account sourceAccount;
    private Account destinationAccount;
    private String category;

    public Transaction(){};
    
    public Transaction(LocalDateTime date, TransactionType transactionType, BigDecimal amount,
        String description, Account sourceAccount, Account destinationAccount, String category) {
        this.transactionId = UUID.randomUUID().toString();
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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

    @Override
    public String toString() {
        return String.format("Transacción ID: %s\nFecha: %s\nTipo: %s\nMonto: $%s\nDescripción: %s\nDe: %s\nA: %s\nCategoría: %s",
                transactionId, date, transactionType, amount, description, 
                sourceAccount != null ? sourceAccount.getAccountNumber() : "N/A",
                destinationAccount != null ? destinationAccount.getAccountNumber() : "N/A",
                category);
    }
}