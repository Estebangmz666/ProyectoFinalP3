package com.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.model.Account;
import com.example.model.Transaction;
import com.example.model.TransactionType;

public class TransactionService {

    public static boolean transfer(Account source, Account destination, BigDecimal amount) {
        if (source.getBalance().compareTo(amount) < 0) {
            return false;
        }

        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        LocalDateTime now = LocalDateTime.now();
        TransactionType transactionType = TransactionType.TRANSFERENCIA;
        String description = "Transferencia entre cuentas";
        String category = "Transferencias";

        Transaction transaction = new Transaction(
                now,                      // Fecha de la transacción
                transactionType,          // Tipo de transacción
                amount,                   // Monto de la transacción
                description,              // Descripción
                source,                   // Cuenta de origen
                destination,              // Cuenta de destino
                category                  // Categoría
        );
        return true;
    }
}
