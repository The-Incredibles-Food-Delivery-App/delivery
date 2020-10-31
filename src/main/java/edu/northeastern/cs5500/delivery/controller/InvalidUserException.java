package edu.northeastern.cs5500.delivery.controller;

    /** Class representing the checked exception that occurs when the given User is invalid */
public class InvalidUserException extends Exception {
    private static final long serialVersionUID = -9065390513206215021L;

    public InvalidUserException(String message) {
        super(message);
    }
}
