package edu.northeastern.cs5500.delivery.controller;

public class DuplicateKeyException extends Exception {
    // private static final long serialVersionUID = 2L;
    private static final long serialVersionUID = -5740262059784130741L;

    /**
     * Constructs DuplicateKeyException object
     *
     * @param message - A message informing the user of an instance of a duplicate key
     */
    public DuplicateKeyException(String message) {
        super(message);
    }
}
