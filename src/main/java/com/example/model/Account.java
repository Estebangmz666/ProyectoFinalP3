package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int accountId;
    private final String accountNumber;
    private final AccountType accountType;
    private BigDecimal balance;

    public Account(int accountId, String accountNumber, AccountType accountType, BigDecimal balance) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public String toString() {
        String maskedAccountNumber = accountNumber.substring(0, 4) + "****" + accountNumber.substring(accountNumber.length() - 4);
        return "Account [accountId=" + accountId + ", accountNumber=" + maskedAccountNumber + ", accountType=" + accountType + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account other = (Account) obj;
        return accountId == other.accountId && 
               Objects.equals(accountNumber, other.accountNumber) &&
               accountType == other.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, accountType);
    }
}
