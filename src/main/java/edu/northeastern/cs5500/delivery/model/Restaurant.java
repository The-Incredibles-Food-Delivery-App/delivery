package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import lombok.Data;
import org.bson.types.ObjectId;

// @ Data annotation creates a constructor, getters, and setters
@Data
public class Restaurant implements Model {
    private String restaurantName;
    private String website;
    private ObjectId id;
    private String phoneNumber;
    private String address;
    private CuisineType cuisineType;
    private String hours;
    // key is the id of the menu item, value is the menu item object itself
    private HashMap<String, MenuItem> menuItems;

    /**
     * A valid restuarant should have a name, a phone number, address, hours, and a menu
     *
     * @return true if this is a valid Restaurant
     */
    @JsonIgnore
    public boolean isValid() {
        return restaurantName != null
                && phoneNumber != null
                && address != null
                && menuItems != null
                && hours != null;
    }
}
