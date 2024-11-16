package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private int accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private User referenceUser;
}