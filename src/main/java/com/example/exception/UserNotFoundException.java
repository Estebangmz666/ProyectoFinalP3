package com.example.exception;

/**
 * UserNotFoundException:
 * Se lanza cuando se intenta buscar un usuario en el sistema con un identificador (ID o correo electrónico) que no existe.
 * Asegura que solo se realicen acciones sobre usuarios existentes en el sistema.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
