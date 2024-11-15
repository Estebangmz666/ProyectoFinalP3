package com.example.exception;

/**
 * PasswordMismatchException:
 * Se lanza cuando las contraseñas proporcionadas para la creación de una cuenta o cambio de contraseña no coinciden.
 * Impide la creación de cuentas o el cambio de contraseñas con valores no coincidentes.
 */
 
public class PasswordMismatchException extends Exception {
    public PasswordMismatchException(String message) {
        super(message);
    }
}

