package edu.northeastern.cs5500.delivery.model;

/** Class representing the checked exception that occurs when the given review is invalid */
public class InvalidReviewException extends Exception {

   
    public InvalidReviewException(String message) {
        super(message);
    }
}