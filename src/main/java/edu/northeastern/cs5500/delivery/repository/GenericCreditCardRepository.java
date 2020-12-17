package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import java.util.Collection;

/**
 * Represents a generic repository containing credit cards. A Generic credit card Repository has a
 * method that returns the deault credit cards for the given user name
 */
public interface GenericCreditCardRepository extends GenericRepository<CreditCard> {

    /**
     * Given a username name, returns the user's default credit cards
     *
     * @param username - the username of the default credit card to search for
     * @return the user's default credit cards
     */
    public Collection<CreditCard> getUserDefaultCard(String username);
}
