package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CreditCardController {
  private final GenericRepository<CreditCard> creditCards;

    @Inject
    CreditCardController(GenericRepository<CreditCard> creditCardRepository) {
        creditCards = creditCardRepository;

        log.info("CreditCardController > construct");

        if (creditCards.count() > 0) {
            return;
        }

        log.info("CreditCardController > construct > adding default creditCards");

        final CreditCard defaultCreditCard1 = new CreditCard();
        // defaultDelivery1.setDistance(1.25);
        // Order defaultOrder1 = new Order();
        // ArrayList<HashMap<String, Integer>> items = new ArrayList<>();
        // HashMap<String, Integer> item1 = new HashMap<>();
        // item1.put("Masala dosa", 1);
        // items.add(item1);
        // defaultOrder1.setItems(items);
        // defaultOrder1.setCost(8.99);
        // defaultOrder1.setOrderTime(LocalDateTime.now());
        // defaultDelivery1.setOrder(defaultOrder1);
        // defaultDelivery1.setNotes("Place in the basket on the front porch");
        // defaultDelivery1.setDeliveryStatus(DeliveryStatus.ENROUTE);

        // final Delivery defaultDelivery2 = new Delivery();
        // defaultDelivery2.setTitle("A steak");
        // defaultDelivery2.setDescription("Not a hot dog");
        // defaultDelivery2.setDistance(21.0);

        try {
            addCreditCard(defaultCreditCard1);
            // addDelivery(defaultDelivery2);
        } catch (Exception e) {
            log.error("CreditCardController > construct > adding default creditCards > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public CreditCard getCreditCard(@Nonnull ObjectId uuid) {
        log.debug("CreditCardController > getCreditCard({})", uuid);
        return creditCards.get(uuid);
    }

    @Nonnull
    public Collection<CreditCard> getCreditCards() {
        log.debug("CreditCardController > getCreditCards()");
        return creditCards.getAll();
    }

    /**
     * Validates the cardnumber. A valid cardnumber is equal to the valid number of digits allowed in a creditcard than or equal to the maximum allowed
     * distance
     *
     * @param creditCard - the creditcard to be validated
     * @throws InvalidCreditCardException if the creditcard number exceeds or is less than the number of allowed digits on a card
     * @return true if the creditcard number is equal to the number of allowed digits
     */
    public boolean verifyCardNumber(@Nonnull CreditCard creditCard) throws InvalidCreditCardException {
        int digits_on_card = 0;
        for (int i=0; i< creditCard.getCardNumber(); i++) {
          digits_on_card++;
        }
        if (digits_on_card != CreditCard.DIGITS_ALLOWED_ON_CARD) {
            String message =
                    "Credit card number is not equal to the allowed"
                            + CreditCard.DIGITS_ALLOWED_ON_CARD
                            + " digits on card.";
            throw new InvalidCreditCardException(message);
        } else {
            return true;
        }
    }

    /**
     * Validates the credit card's expiration data. A valid expiration date is greater than today's local date
     *
     * @param creditCard - the creditcard to be validated
     * @throws InvalidCreditCardException if the creditcard's expiration date is prior to todays date
     * @return true if the creditcard's expiration date is equal to today or later
     */
    public boolean verifyCardExpirationDate(@Nonnull CreditCard creditCard) throws InvalidCreditCardException {
      if (creditCard.getExpirationDate().isBefore(LocalDate.now())) {
          String message =
                  "Credit card expiration date is before the current day and has thus expired.";
          throw new InvalidCreditCardException(message);
      } else {
          return true;
      }
  }

    @Nonnull
    public CreditCard addCreditCard(@Nonnull CreditCard creditCard)
            throws DuplicateKeyException, InvalidDeliveryException {
        log.debug("CreditCardController > addCreditCard(...)");
        if (!creditCard.isValid() || !this.verifyCardNumber(creditCard) || !this.verifyCardExpirationDate(creditCard)) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new InvalidCreditCardException("Invalid CreditCard");
        }

        ObjectId id = creditCard.getId();

        if (id != null && creditCards.get(id) != null) {
            throw new DuplicateKeyException("This creditcard already exists");
        }

        return creditCards.add(creditCard);
    }

    public void updateCreditCard(@Nonnull CreditCard creditCard) throws Exception {
        log.debug("CreditCardController > updateCreditCard(...)");
        creditCards.update(creditCard);
    }

    public void deleteCreditCard(@Nonnull ObjectId id) throws Exception {
        log.debug("CreditCardController > deleteCreditCard(...)");
        creditCards.delete(id);
    }
}

}
