package edu.northeastern.cs5500.delivery.controller;

/** Class representing the checked exception that occurs when the given review is invalid */
public class InvalidReviewException extends Exception {
    private static final long serialVersionUID = -4065397513207515022L;

    public InvalidReviewException(String message) {
        super(message);
    }
}
