package com.example.exception;

/**
 * InvalidUserInputException:
 * Se lanza cuando un campo de entrada contiene caracteres no válidos o el formato es incorrecto (por ejemplo, números de cuenta que no son numéricos).
 * Asegura que los usuarios ingresen solo datos válidos y correctamente formateados.
 */
public class InvalidUserInputException extends Exception {
    public InvalidUserInputException(String message) {
        super(message);
    }
}


