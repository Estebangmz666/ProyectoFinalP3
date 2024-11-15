package com.example.exception;

/**
 * AccountAlreadyExistsException: 
 * Se lanza cuando se intenta crear una nueva cuenta con un número de cuenta que ya existe en el sistema.
 * Esto evita la creación de cuentas duplicadas con el mismo número.
 */
public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}

