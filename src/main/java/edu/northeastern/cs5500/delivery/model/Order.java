package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import java.util.*;
import java.time.LocalDateTime;

// @ Data annotation creates a constructor, getters, and setters
@Data
public class Order implements Model {
    private static final Integer MAXIMUM_HOURS_ORDER_IN_ADV = 2;

    private ObjectId id;
    // TODO: create Receipt Model
    // private Receipt receipt;
    private Double cost;
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
    // TODO: work on payment Model and uncomment this
    // private CreditCard payment;


    /** 
    Validates the order. A valid order has a user, a valid associated restaurant,
    and at least one item.
    @return true if this order is valid.
     */
    @JsonIgnore
    public boolean isValid() {
        // && !user.isEmpty() TODO: after user is created
        // user != null 
        return restaurantID != null
               && this.restaurantID != null
               && this.verifyOrderNonempty()
               && this.verifyOrderByTime();
    }

    /**
    Verifies that the order contains at least one item.
    @return true if the order contains at least one item.
     */
    @JsonIgnore
    private boolean verifyOrderNonempty() {
        if (this.items.isEmpty()) {
            return false;
        }
        // If one item in the order, ensure quantity is at least 1
        // TODO: we may just want to add this error checking to the Item itself!
        if (this.items.size() == 1) {
            HashMap<String, Integer> item = this.items.get(0);
            for (String i : item.keySet()) {
                if (item.get(i) < 1) {
                    return false;
                }
            }
        }
       return true;
    }

    /**
    Verifies that the order contains a valid order by time.
    @throws InvalidOrderException - if order time is before the current time
        or more than the maximum number of hours from the current time.
    @return true if a valid order by time has been set
     */
    @JsonIgnore
    private boolean verifyOrderByTime() throws InvalidOrderException {
        // if OrderBy date/time is not set or set to the past, set to current timestamp
        // QUESTION: Do we want to throw an exception if set in the past?
        if (this.orderBy == null || this.orderBy.isBefore(LocalDateTime.now())) {
            this.orderBy = LocalDateTime.now();
        } 
        // if orderBy data/time is set too far in advance, throw an exception
        if (this.orderBy.isAfter(LocalDateTime.now())) {
            if (this.orderBy.getDayOfWeek() != LocalDateTime.now().getDayOfWeek()
               || this.orderBy.getHour() > LocalDateTime.now().getHour + MAXIMUM_HOURS_ORDER_IN_ADV) {
                   throw new InvalidOrderException("Please choose an order time that is within " 
                   + MAXIMUM_HOURS_ORDER_IN_ADV) + " hours of the current time.");
               }
        }

       return true;
    }

}
