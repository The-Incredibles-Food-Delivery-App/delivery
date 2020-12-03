package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
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
        this.initializeCreditCards();
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
     * Validates the cardnumber. A valid cardnumber is equal to the valid number of digits allowed
     * in a creditcard than or equal to the maximum allowed distance
     *
     * @param creditCard - the creditcard to be validated
     * @throws InvalidCreditCardException if the creditcard number exceeds or is less than the
     *     number of allowed digits on a card
     * @return true if the creditcard number is equal to the number of allowed digits
     */
    public boolean verifyCardNumber(@Nonnull CreditCard creditCard)
            throws InvalidCreditCardException {

        int digits_on_card = 0;
        long tempCreditCardNumber = creditCard.getCardNumber();
        while (tempCreditCardNumber > 0) {
            digits_on_card++;
            tempCreditCardNumber /= 10;
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
     * Validates the credit card's expiration data. A valid expiration date is greater than today's
     * local date
     *
     * @param creditCard - the creditcard to be validated
     * @throws InvalidCreditCardException if the creditcard's expiration date is prior to todays
     *     date
     * @return true if the creditcard's expiration date is equal to today or later
     */
    // public boolean verifyCardExpirationDate(@Nonnull CreditCard creditCard)
    //         throws InvalidCreditCardException {
    //     if (creditCard.getExpirationDate().isBefore(LocalDateTime.now())) {
    //         String message =
    //                 "Credit card expiration date is before the current day and has thus
    // expired.";
    //         throw new InvalidCreditCardException(message);
    //     } else {
    //         return true;
    //     }
    // }

    @Nonnull
    public CreditCard addCreditCard(@Nonnull CreditCard creditCard)
            throws DuplicateKeyException, InvalidCreditCardException {
        log.debug("CreditCardController > addCreditCard(...)");
        if (!creditCard.isValid() || !this.verifyCardNumber(creditCard)) {

            // || !this.verifyCardExpirationDate(creditCard)) {

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

    private void initializeCreditCards() {
        log.info("CreditCardController > construct > adding default creditcards");

        final CreditCard defaultCreditCard1 = new CreditCard();
        defaultCreditCard1.setCardNumber(1234567891234567L);
        // defaultCreditCard1.setExpirationDate(LocalDateTime.now());
        defaultCreditCard1.setUsername("Mary Poppins");
        defaultCreditCard1.setIsDefault(false);

        final CreditCard defaultCreditCard2 = new CreditCard();
        defaultCreditCard2.setCardNumber(1234567891234569L);
        // defaultCreditCard2.setExpirationDate(LocalDateTime.now());
        defaultCreditCard2.setUsername("Spider Man");
        defaultCreditCard2.setIsDefault(false);

        try {
            addCreditCard(defaultCreditCard1);
            addCreditCard(defaultCreditCard2);
        } catch (Exception e) {
            log.error("CreditCardController > construct > adding default creditCards > failure?");
            e.printStackTrace();
        }
    }
}
