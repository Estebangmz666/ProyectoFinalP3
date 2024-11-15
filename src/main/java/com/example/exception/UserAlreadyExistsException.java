package com.example.exception;

/**
 * UserAlreadyExistsException:
 * Se lanza cuando se intenta registrar un nuevo usuario con un correo electrónico que ya está registrado en el sistema.
 * Previene la creación de usuarios duplicados con el mismo correo electrónico.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
