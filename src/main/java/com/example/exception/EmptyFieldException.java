package com.example.exception;

/**
 * EmptyFieldException: 
 * Se lanza cuando algún campo necesario para la creación o actualización de datos (como número de cuenta o correo electrónico) está vacío.
 * Garantiza que todos los campos sean completados antes de realizar cualquier acción.
 */
public class EmptyFieldException extends Exception {
    public EmptyFieldException(String message) {
        super(message);
    }
}

