package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
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
    void testCanAddDelivery() throws DuplicateKeyException, InvalidCreditCardException{
        CreditCardController creditCardController = new CreditCardController(new InMemoryRepository<CreditCard>());
        
        // Creates a default creditcard
        final CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(1234123412341234L);
        testCreditCard.setExpirationDate(LocalDate.now());
        testCreditCard.setUsername("Jimmy Neutron");
        testCreditCard.setIsDefault(true);

        // check that the creditcard has been added to the CreditCard repository
        CreditCard addedCreditCard = creditCardController.addCreditCard(testCreditCard);
        assertEquals(testCreditCard.getCardNumber(), addedCreditCard.getCardNumber());
        assertEquals(testCreditCard.getExpirationDate(), addedCreditCard.getExpirationDate());
        assertEquals(testCreditCard.getUsername(), addedCreditCard.getUsername());
        assertEquals(testCreditCard.getIsDefault(), addedCreditCard.getIsDefault());
    }

    @Test
    void testCanReplaceDelivery() {
        // This test should NOT call register
        // TODO: implement this test.
    }

    @Test
    void testCanDeleteDelivery() {
        // This test should NOT call register
        // TODO: implement this test
    }
}
