package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class CreditCardTest {
    @Test
    void testRegisterCreatesCreditCard() {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryRepository<CreditCard>());
        assertThat(creditCardController.getCreditCards()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidDeliveries() {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryRepository<CreditCard>());

        for (CreditCard creditCard : creditCardController.getCreditCards()) {
            assertTrue(creditCard.isValid());
        }
    }

    @Test
    void testCanAddDelivery() throws DuplicateKeyException, InvalidCreditCardException {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryRepository<CreditCard>());

        // Creates a default creditcard
        final CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(1234123412341234L);
        testCreditCard.setExpirationDate(LocalDate.now());
        testCreditCard.setUsername("Jimmy Neutron");
        testCreditCard.setIsDefault(false);

        // check that the creditcard has been added to the CreditCard repository
        CreditCard addedCreditCard = creditCardController.addCreditCard(testCreditCard);
        assertEquals(testCreditCard.getCardNumber(), addedCreditCard.getCardNumber());
        assertEquals(testCreditCard.getExpirationDate(), addedCreditCard.getExpirationDate());
        assertEquals(testCreditCard.getUsername(), addedCreditCard.getUsername());
        assertEquals(testCreditCard.getIsDefault(), addedCreditCard.getIsDefault());
    }

    @Test
    void testCanReplaceDelivery()
            throws DuplicateKeyException, InvalidCreditCardException, Exception {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryRepository<CreditCard>());

        // Creates a default creditcard
        final CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(1234123412341234L);
        testCreditCard.setExpirationDate(LocalDate.now());
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
    void testCanDeleteDelivery() throws Exception {
        CreditCardController creditCardController =
                new CreditCardController(new InMemoryRepository<CreditCard>());

        // Creates a default creditcard
        final CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(1234123412341234L);
        testCreditCard.setExpirationDate(LocalDate.now());
        testCreditCard.setUsername("Jimmy Neutron");
        testCreditCard.setIsDefault(false);

        // check that the creditcard has been added to the CreditCard repository
        CreditCard addedCreditCard = creditCardController.addCreditCard(testCreditCard);

        // Delete the credit card we just added
        creditCardController.deleteCreditCard(addedCreditCard.getId());

        // Iterate through the in-memory repo of credit card, and flag is the card was deleted
        boolean cardIsNotDeleted = false;
        for (CreditCard creditCard : creditCardController.getCreditCards()) {
            if (creditCard == addedCreditCard) {
                cardIsNotDeleted = true;
                break;
            }
        }
        assertEquals(true, cardIsNotDeleted);
    }
}
