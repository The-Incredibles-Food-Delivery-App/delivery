package edu.northeastern.cs5500.delivery.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

// @ Data annotation creates a constructor, getters, and setters
@Data
public class Restaurant implements Model {
    private static final Double MAX_DISTANCE = 5.0;
    
    private String restaurantName;
    private String website;
    // confused about the objectId vs restaurantId, went with objectId?
    private ObjectId id;
    // private Integer restaurantId;
    private Integer phoneNumber;
    private String address;
    // Uncomment this out after adding cuisine enum
    // CuisineType cuisineType;
    private String hours;
    private ArrayList<ArrayList<Order>> pendingOrders;
    private ArrayList<HashMap<String, String>> menu;

    /** A valid restuarant should have a name, a phone number,
     * hours, and a menu
     * @return true if this is a valid Restaurant
     */
    @JsonIgnore
    public boolean isValid() throws InvalidOrderException {
        return restaurantName != null
                && phoneNumber != null
                && menu != null
                && hours != null;
    }

}