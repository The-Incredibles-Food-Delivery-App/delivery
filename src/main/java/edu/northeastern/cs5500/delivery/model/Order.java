package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import java.util.*;
import java.time.LocalDateTime;

// @ Data annotation creates a constructor, getters, and setters
@Data
public class Order implements Model {
    private ObjectId id;
    // TODO: create Receipt Model
    // private Receipt receipt;
    private String currency;
    // TODO: In the future, replace String with Item for a
    // food item
    private ArrayList<HashMap<String, Integer>> items;
    private LocalDateTime orderTime;
    private LocalDateTime completionTime;
    // TODO: Make sure enum is completed & uncomment this
    // private OrderStatus orderStatus;
    private double distance;
    private LocalDateTime orderBy;
    // TODO: Make sure User class is completed & uncomment this
    // Can we use dep injection for this?
    // private User user;
    private Integer restaurantID;


    /** 
    Validates the order. A valid order has a user, a valid associated restaurant,
    and at least one item.
    @return true if this order is valid.
     */
    @JsonIgnore
    public boolean isValid() {
        return user != null && !user.isEmpty()
               && restaurantID != null
               && !restaurantID.isEmpty()
               && this.verifyOrderNonempty()
    }

    /**
    Verifies that the order contains at least one item.
    @return true if the order contains at least one item.
     */
    @JsonIgnore
    private boolean verifyOrderNonempty() {
        if (this.items.isEmpty()) {
            return false
        }
        // If one item in the order, ensure quantity is at least 1
        // TODO: we may just want to add this error checking to the Item itself!
        if (this.items.size() == 1) {
            HashMap<String, Integer> item = this.items.get(0);
            for (String i : item.keySet()) {
                if (item.get(i) < 1) {
                    return False
                }
            }
        }
       return true;
    }
