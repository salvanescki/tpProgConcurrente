package org.example;

/**
 * Excepción lanzada con el objetivo de matar un Worker mediante el Buffer.
 */
public class PoisonException extends RuntimeException {
    public PoisonException(String message) {
        super(message);
    }
}
