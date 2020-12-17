package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.bson.Document;

public class MongoDBCreditCardRepository extends MongoDBRepository<CreditCard>
        implements GenericCreditCardRepository {

    @Inject
    public <T> MongoDBCreditCardRepository(MongoDBService mongoDBService) {
        super(CreditCard.class, mongoDBService);
        collection.createIndex(new Document("username", "text"));
    }

    @Override
    public Collection<CreditCard> getUserDefaultCard(String username) {
        Document regQuery = new Document();
        regQuery.append("$regex", "^(?)" + Pattern.quote(username));
        regQuery.append("$options", "i");

        Document findQuery = new Document();
        findQuery.append("username", regQuery);
        return collection.find(findQuery).into(new ArrayList<>());
    }
}
