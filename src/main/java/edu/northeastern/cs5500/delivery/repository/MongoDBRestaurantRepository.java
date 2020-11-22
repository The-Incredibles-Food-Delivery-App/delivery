package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

public class MongoDBRestaurantRepository extends MongoDBRepository<Restaurant>
        implements GenericRestaurantRepository {

    @Inject
    public MongoDBRestaurantRepository(MongoDBService mongoDBService) {
        super(Restaurant.class, mongoDBService);
    }

    @Override
    public Collection<Restaurant> getRestaurantByName(String name) {
        // TODO: how to filter on name? {"restaurantName" : name}
        // TODO: Index the Restaurants collection by name for faster lookup
        return collection.find().into(new ArrayList<>());
    }
}
