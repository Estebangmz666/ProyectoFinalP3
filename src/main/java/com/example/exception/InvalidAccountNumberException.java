package com.example.exception;

/**
 * InvalidAccountNumberException:
 * Se lanza cuando el número de cuenta proporcionado no tiene el formato correcto o no cumple con las reglas de validación (por ejemplo, longitud incorrecta).
 * Asegura que los números de cuenta sean válidos antes de continuar con la creación de cuentas o transacciones.
 */
public class InvalidAccountNumberException extends Exception {
    public InvalidAccountNumberException(String message) {
        super(message);
    }
}

