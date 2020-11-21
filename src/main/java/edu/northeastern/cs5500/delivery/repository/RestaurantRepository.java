package edu.northeastern.cs5500.delivery.repository;

import java.util.Collection;

import edu.northeastern.cs5500.delivery.model.Restaurant;


public abstract class RestaurantRepository implements GenericRepository<Restaurant> {

    /**
     * Given a restaurant name, returns the restaurant object
     * @param name - the name of the restaurant to retrieve
     * @return the restaurant corresponding to the given name
     */
    public abstract Collection<Restaurant> getRestauarantByName(String name);
    
}
