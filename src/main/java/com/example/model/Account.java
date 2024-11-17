package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class Account implements Serializable {
    private static int idCounter; 
    private static final long serialVersionUID = 1L;
    private int accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private User referenceUser;

    public Account(int accountId, String accountNumber, AccountType accountType, BigDecimal balance,
            User referenceUser) {
        if(accountId == 0){
            int newId = ++Account.idCounter;
            Account.idCounter = newId;
            this.accountId = newId;
        }else{
            this.accountId = accountId;
        }
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.referenceUser = referenceUser;
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.accountType = AccountType.AHORROS;
        this.referenceUser = null;
    }

    public static int getIdCounter(){
        return Account.idCounter;
    }

    public static void setIdCounter(int id){
        Account.idCounter = id;
    }
}