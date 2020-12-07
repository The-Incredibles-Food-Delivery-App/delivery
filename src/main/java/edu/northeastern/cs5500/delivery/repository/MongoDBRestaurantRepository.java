package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.bson.Document;

public class MongoDBRestaurantRepository extends MongoDBRepository<Restaurant>
        implements GenericRestaurantRepository {

    @Inject
    public <T> MongoDBRestaurantRepository(MongoDBService mongoDBService) {
        super(Restaurant.class, mongoDBService);
        collection.createIndex(new Document("restaurantName", "text"));
    }

    @Override
    public Collection<Restaurant> getRestaurantByName(String name) {
        /* UPDATE (CSM):
        I think this is how u would do that?
        Collection<Restaurant> restaurants = this.getAll();
        Collection<Restaurant> filteredRestaurants = new ArrayList<>();
        restaurants.stream().filter(restaurant -> restaurant.getRestaurantName().equals(name));
        // add this to the array
        */

        // TODO: Index the Restaurants collection by name for faster lookup
        // TODO: Figure out how to use this filter with the supplied name parameter
        // String regexExpr = String.format("/^%s/i", name);
        Document regQuery = new Document();
        regQuery.append("$regex", "^(?)" + Pattern.quote(name));
        regQuery.append("$options", "i");

        Document findQuery = new Document();
        findQuery.append("restaurantName", regQuery);
        // Document r = new Document("$regex", "/^C/");
        // return collection.find(new Document("restaurantName", r)).into(new ArrayList<>());
        return collection.find(findQuery).into(new ArrayList<>());
    }
}
