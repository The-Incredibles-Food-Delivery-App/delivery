package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.Restaurant;
import java.util.Collection;

/**
 * Represents a generic repository containing restaurants. A Generic Restaurantn Repository has a
 * method that returns the restaurants with the given name.
 */
public interface GenericRestaurantRepository extends GenericRepository<Restaurant> {

    /**
     * Given a restaurant name, returns the restaurant object
     *
     * @param name - the name of the restaurant to retrieve
     * @return the restaurant corresponding to the given name
     */
    public Collection<Restaurant> getRestaurantByName(String name);
}
