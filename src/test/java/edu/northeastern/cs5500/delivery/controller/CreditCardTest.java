package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.InMemoryCreditCardRepository;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class CreditCardTest {
    @Test
    void testRegisterCreatesCreditCard() {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryCreditCardRepository());
        assertThat(creditCardController.getCreditCards()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidCreditCards() {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryCreditCardRepository());
        for (CreditCard creditCard : creditCardController.getCreditCards()) {
            assertTrue(creditCard.isValid());
        }
    }

    @Test
    void testGetDefaultCreditCard() {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryCreditCardRepository());
        Collection<CreditCard> results = creditCardController.getDefaultCreditCard("Mary Poppins");
        // user has one default card
        assertEquals(1, results.size());
        for (CreditCard card : results) {
            assertTrue(1234567891234567L == card.getCardNumber());
        }
        // user has no default cards
        Collection<CreditCard> results2 = creditCardController.getDefaultCreditCard("Spider Man");
        assertEquals(0, results2.size());
    }

    @Test
    void testCanAddCreditCard() throws DuplicateKeyException, InvalidCreditCardException {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryCreditCardRepository());
        // Creates a default creditcard
        final CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(1234123412341234L);
        // testCreditCard.setExpirationDate(LocalDateTime.now());
        testCreditCard.setUsername("Jimmy Neutron");
        testCreditCard.setIsDefault(false);

        // check that the creditcard has been added to the CreditCard repository
        CreditCard addedCreditCard = creditCardController.addCreditCard(testCreditCard);
        assertEquals(testCreditCard.getCardNumber(), addedCreditCard.getCardNumber());
        // assertEquals(testCreditCard.getExpirationDate(), addedCreditCard.getExpirationDate());
        assertEquals(testCreditCard.getUsername(), addedCreditCard.getUsername());
        assertEquals(testCreditCard.getIsDefault(), addedCreditCard.getIsDefault());
    }

    @Test
    void testCanReplaceCreditCard()
            throws DuplicateKeyException, InvalidCreditCardException, Exception {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryCreditCardRepository());

        // Creates a default creditcard
        final CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(1234123412341234L);
        // testCreditCard.setExpirationDate(LocalDateTime.now());
        testCreditCard.setUsername("Jimmy Neutron");
        testCreditCard.setIsDefault(false);

        // check that the creditcard has been added to the CreditCard repository
        CreditCard addedCreditCard = creditCardController.addCreditCard(testCreditCard);
        // Update the creditcard's field
        addedCreditCard.setCardNumber(1234123412341235L);
        creditCardController.updateCreditCard(addedCreditCard);

        assertTrue(addedCreditCard.getCardNumber().equals(1234123412341235L));
    }

    @Test
    void testCanDeleteCreditCard() throws Exception {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryCreditCardRepository());

        // Creates a default creditcard
        final CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(1234123412341234L);
        // testCreditCard.setExpirationDate(LocalDateTime.now());
        testCreditCard.setUsername("Jimmy Neutron");
        testCreditCard.setIsDefault(false);

        // check that the creditcard has been added to the CreditCard repository
        CreditCard addedCreditCard = creditCardController.addCreditCard(testCreditCard);

        // Delete the credit card we just added
        creditCardController.deleteCreditCard(addedCreditCard.getId());

        // Iterate through the in-memory repo of credit card, and flag is the card was deleted
        boolean cardIsDeleted = true;
        for (CreditCard creditCard : creditCardController.getCreditCards()) {
            if (creditCard == addedCreditCard) {
                cardIsDeleted = false;
                break;
            }
        }
        assertEquals(true, cardIsDeleted);
    }
}
