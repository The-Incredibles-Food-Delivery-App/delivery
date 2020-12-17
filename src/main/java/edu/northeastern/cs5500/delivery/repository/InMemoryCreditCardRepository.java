package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

public class InMemoryCreditCardRepository extends InMemoryRepository<CreditCard>
        implements GenericCreditCardRepository {

    @Inject
    public InMemoryCreditCardRepository() {
        super();
    }

    @Override
    public Collection<CreditCard> getUserDefaultCard(String username) {
        Collection<CreditCard> creditCards = this.getAll();
        Collection<CreditCard> results = new ArrayList<>();
        for (CreditCard card : creditCards) {
            if (card.getUsername().equals(username) && card.getIsDefault() == true) {
                results.add(card);
            }
        }
        return results;
    }
}
