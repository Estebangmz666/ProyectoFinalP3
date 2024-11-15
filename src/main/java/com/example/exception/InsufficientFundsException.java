package com.example.exception;

/**
 * InsufficientFundsException:
 * Se lanza cuando un intento de transferencia de dinero no es posible debido a fondos insuficientes en la cuenta fuente.
 * Impide realizar transacciones sin los fondos suficientes.
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

