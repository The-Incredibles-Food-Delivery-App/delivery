package edu.northeastern.cs5500.delivery.controller;

public class OrderIncompleteException extends Exception {
    private static final long serialVersionUID = 2177399255031007282L;

    /**
     * Constructs an InvalidObjectException object
     *
     * @param message - A message informing the user why their object is invalid
     */
    public OrderIncompleteException(String message) {
        super(message);
    }
}
