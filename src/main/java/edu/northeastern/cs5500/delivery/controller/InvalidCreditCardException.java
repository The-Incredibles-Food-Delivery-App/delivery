package edu.northeastern.cs5500.delivery.controller;

/** Class representing the checked exception that occurs when the given creditcard is invalid */
public class InvalidCreditCardException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an InvalidDateException object
     *
     * @param message - A message informing the user why their creditcard is invalid
     */
    public InvalidCreditCardException(String message) {
        super(message);
    }
}
