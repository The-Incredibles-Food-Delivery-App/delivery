package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Review implements Model {
    private ObjectId id;
    private double rating;
    private String username;
    private Restaurant restaurantID;
    private String reviewContent;
    private LocalDateTime timeStamp;

    /**
     * Validates the Review. A valid review has a valid rating, a valid user, a valid associated
     * restaurant, and a valid timestamp.
     *
     * @return true if this Review is valid.
     */
    @JsonIgnore
    public boolean isValid() {
        return username != null && restaurantID != null && timeStamp != null;
    }
}
