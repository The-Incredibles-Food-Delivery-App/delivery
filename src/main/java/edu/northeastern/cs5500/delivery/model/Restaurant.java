package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Data;
import org.bson.types.ObjectId;

// @ Data annotation creates a constructor, getters, and setters
@Data
public class Restaurant implements Model {
    // Maximum distance a restaurant will send food out?
    // private static final Double MAX_DISTANCE = 5.0;
    private String restaurantName;
    private String website;
    private ObjectId id;
    private String phoneNumber;
    private String address;
    private CuisineType cuisineType;
    private String hours;
    private ArrayList<ArrayList<Order>> pendingOrders;
    // Arraylist for the purposes of breaking down breakfast/lunch/dinner of items with their
    // pricing
    private HashMap<String, HashMap<String, Double>> menu;

    /**
     * A valid restuarant should have a name, a phone number, hours, and a menu
     *
     * @return true if this is a valid Restaurant
     */
    @JsonIgnore
    public boolean isValid() {
        return restaurantName != null
                && phoneNumber != null
                && address != null
                && menu != null
                && hours != null;
    }
}
