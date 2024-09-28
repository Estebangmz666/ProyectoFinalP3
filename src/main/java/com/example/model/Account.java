package com.example.model;

import java.io.Serializable;

public class Account implements Serializable{
    private int userId;
    private String accountId;
    private String accountNumber;
    private AccountType accountType;

    public Account(String accountId, String accountNumber, AccountType accountType){
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }
    
    public int getUserId() {
        return userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}