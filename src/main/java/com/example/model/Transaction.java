package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private StringProperty transactionId;
    private SimpleObjectProperty<LocalDateTime> date;
    private SimpleObjectProperty<TransactionType> transactionType;
    private SimpleObjectProperty<BigDecimal> amount;
    private StringProperty description;
    private SimpleObjectProperty<Account> sourceAccount;
    private SimpleObjectProperty<Account> destinationAccount;
    private StringProperty category;

    public Transaction(String transactionId, LocalDateTime date, TransactionType transactionType, BigDecimal amount, 
                      String description, Account sourceAccount, Account destinationAccount, String category) {
        this.transactionId = new SimpleStringProperty(transactionId);
        this.date = new SimpleObjectProperty<>(date);
        this.transactionType = new SimpleObjectProperty<>(transactionType);
        this.amount = new SimpleObjectProperty<>(amount);
        this.description = new SimpleStringProperty(description);
        this.sourceAccount = new SimpleObjectProperty<>(sourceAccount);
        this.destinationAccount = new SimpleObjectProperty<>(destinationAccount);
        this.category = new SimpleStringProperty(category);
    }

    public String getTransactionId() {
        return transactionId.get();
    }

    public void setTransactionId(String transactionId) {
        this.transactionId.set(transactionId);
    }

    public StringProperty transactionIdProperty() {
        return transactionId;
    }

    public LocalDateTime getDate() {
        return date.get();
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public TransactionType getTransactionType() {
        return transactionType.get();
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType.set(transactionType);
    }

    public SimpleObjectProperty<TransactionType> transactionTypeProperty() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount.get();
    }

    public void setAmount(BigDecimal amount) {
        this.amount.set(amount);
    }

    public SimpleObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public Account getSourceAccount() {
        return sourceAccount.get();
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount.set(sourceAccount);
    }

    public SimpleObjectProperty<Account> sourceAccountProperty() {
        return sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount.get();
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount.set(destinationAccount);
    }

    public SimpleObjectProperty<Account> destinationAccountProperty() {
        return destinationAccount;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }
}