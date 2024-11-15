package com.example.exception;

/**
 * InvalidEmailDomainException:
 * Se lanza cuando se intenta ingresar un correo electrónico que no pertenece a un dominio válido predefinido (como Gmail, Yahoo, etc.).
 * Asegura que solo se usen correos electrónicos de dominios permitidos.
 */
public class InvalidEmailDomainException extends Exception {
    public InvalidEmailDomainException(String message) {
        super(message);
    }
}

