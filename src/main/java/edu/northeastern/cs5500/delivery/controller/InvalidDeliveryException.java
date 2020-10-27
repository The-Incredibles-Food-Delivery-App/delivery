package edu.northeastern.cs5500.delivery.controller;

/** Class representing the checked exception that occurs when the given delivery is invalid */
public class InvalidDeliveryException extends Exception {
    private static final long serialVersionUID = -8235267139044214643L;

    /**
     * Constructs an InvalidDateException object
     *
     * @param message - A message informing the user why their order is invalid
     */
    public InvalidDeliveryException(String message) {
        super(message);
    }
}
