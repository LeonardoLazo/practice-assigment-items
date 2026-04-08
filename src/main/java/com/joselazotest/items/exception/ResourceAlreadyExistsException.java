package com.joselazotest.items.exception;

/**
 * Thrown to indicate that a requested resource cannot be created because it already exists.
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the conflict
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
