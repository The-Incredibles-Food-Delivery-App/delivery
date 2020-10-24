package edu.northeastern.cs5500.delivery.model;

/** Class representing the checked exception that occurs when the given order is invalid */
public class InvalidOrderException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an InvalidDateException object
     *
     * @param message - A message informing the user why their order is invalid
     */
    public InvalidOrderException(String message) {
        super(message);
    }
}
