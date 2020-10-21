package edu.northeastern.cs5500.delivery.model;

/** Class representing the checked exception that occurs when the given recommendation is invalid */
public class InvalidRecommendationException extends Exception {

   
    public InvalidRecommendationException(String message) {
        super(message);
    }
}