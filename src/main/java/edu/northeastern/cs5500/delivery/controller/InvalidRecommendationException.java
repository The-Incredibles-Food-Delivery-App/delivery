package edu.northeastern.cs5500.delivery.controller;

/** Class representing the checked exception that occurs when the given recommendation is invalid */
public class InvalidRecommendationException extends Exception {
    private static final long serialVersionUID = 5402188650714772796L;

    public InvalidRecommendationException(String message) {
        super(message);
    }
}
