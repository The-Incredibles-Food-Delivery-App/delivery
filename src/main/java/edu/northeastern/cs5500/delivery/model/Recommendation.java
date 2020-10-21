package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

// @ Data annotation creates a constructor, getters, and setters
@Data
public class Recommendation implements Model {
    private ObjectId id;
    private String username;
    private Integer restaurantID;

    /**
     * Validates the order. A valid order has a user, a valid associated restaurant, and at least
     * one item.
     *
     * @return true if this Recommendation is valid.
     */
    @JsonIgnore
    public boolean isValid() throws InvalidRecommendationException{
      // add this when the user is User model is created && user.isEmpty() != null 
        return  username != null && restaurantID != null;
    }
}