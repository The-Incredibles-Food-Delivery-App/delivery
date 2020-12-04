package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class CreditCard implements Model {
    public static final Integer DIGITS_ALLOWED_ON_CARD = 16;

    private ObjectId id;
    private Long cardNumber;
    // Removed the expirationDate because the POST request in the CreditCard View will not
    // deserialize the LocalDateTime object to a string
    // private LocalDateTime expirationDate;
    private String username;
    private Boolean isDefault;

    /**
     * Checks that the CreditCard is valid, a valid creditCard has a nonnull cardNumber, non null
     * expiration date and non null username.
     *
     * @return true if this CreditCard is valid
     */
    @JsonIgnore
    public boolean isValid() {
        return this.cardNumber != null && this.username != null;
    }
}
