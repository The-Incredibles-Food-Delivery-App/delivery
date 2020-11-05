package edu.northeastern.cs5500.delivery.controller;

/**
 * Class representing the checked exception that occurs when the given object is invalid
 */
public class InvalidObjectException extends Exception {
	private static final long serialVersionUID = 3177382255042003682L;

	/**
     * Constructs an InvalidObjectException object
     *
     * @param message - A message informing the user why their object is invalid
     */
    public InvalidObjectException(String message) {
        super(message);
    }
}
