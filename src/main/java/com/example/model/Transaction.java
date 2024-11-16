package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

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
}