package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.Restaurant;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

public class InMemoryRestaurantRepository extends InMemoryRepository<Restaurant>
        implements GenericRestaurantRepository {

    @Inject
    public InMemoryRestaurantRepository() {
        super();
    }

    @Override
    public Collection<Restaurant> getRestaurantByName(String name) {
        Collection<Restaurant> restaurants = this.getAll();
        Collection<Restaurant> results = new ArrayList<Restaurant>();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getRestaurantName().equals(name)) {
                results.add(restaurant);
            }
        }
        return results;
    }
}
