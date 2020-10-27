package edu.northeastern.cs5500.delivery.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class CreditCard implements Model{
  public static final Integer DIGITS_ALLOWED_ON_CARD = 16;

  private ObjectId id;
  private Long cardNumber;
  private LocalDate expirationDate;
  private String username;
  // TODO: isDefault was on the UML but unsure what this meant
  private Boolean isDefault;

  /**
   * Checks that the CreditCard is valid, a valid creditCard has a nonnull cardNumber, non null expiration date and non null username.
   *
   * @return true if this CreditCard is valid
   */
  @JsonIgnore
  public boolean isValid() {
      return cardNumber != null && expirationDate != null && username != null;
  }
}
