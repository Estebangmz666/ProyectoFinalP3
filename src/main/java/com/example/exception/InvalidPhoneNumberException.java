package com.example.exception;

/**
 * InvalidPhoneNumberException:
 * Se lanza cuando el número de teléfono proporcionado no cumple con el formato esperado (por ejemplo, si contiene caracteres no numéricos).
 * Garantiza que los números de teléfono ingresados sean válidos.
 */
public class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}

