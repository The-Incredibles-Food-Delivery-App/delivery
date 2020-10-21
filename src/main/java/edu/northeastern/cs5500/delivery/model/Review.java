package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

@Data
public class Review implements Model {
    private ObjectId id;
    private double rating;
    private String username;
    private Resturant restaurantID;
    private String reviewContent;
    private LocalDateTime timeStamp;

    /**
     * Validates the Review. A valid review has a valid rating, a valid user, a valid associated restaurant, and a valid timestamp.
     *
     * @return true if this Review is valid.
     */
    @JsonIgnore
    public boolean isValid() throws InvalidReviewException{
        return ratingIsValid() && username != null && restaurantID != null && timestampIsValid();
    }

    /**
     * Validates the ReviewRating is in the range of valid numbers. 
     * @return true if this ReviewRating is valid.
     */
    @JsonIgnore
    public boolean ratingIsValid() throws InvalidReviewException{
        if (this.rating < 0.0 || this.rating > 5.0) {
          return false;
        } 
        return true;
    }

    /**
     * Validates the timestamp of the review is before the present time. 
     * @return true if this timestamp is valid.
     */
    @JsonIgnore
    public boolean timestampIsValid() throws InvalidReviewException{
        if (this.timeStamp.isAfter(LocalDateTime.now())) {
          return false;
        } 
        return true;
    }


} 