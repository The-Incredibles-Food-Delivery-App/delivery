package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.*;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Order implements Model {
    public static final Integer MAXIMUM_HOURS_ORDER_IN_ADV = 2;
    public static final Integer MAXIMUM_DISTANCE = 35;

    private ObjectId id;
    private Double cost;
    private String currency;
    private ArrayList<HashMap<String, Integer>> items;
    private LocalDateTime orderTime;
    private LocalDateTime completionTime;
    private OrderStatus orderStatus;
    private double distance;
    private LocalDateTime orderBy;
    // TODO: Is this the correct way to incorporate User?
    private User user;
    private Integer restaurantId;
    // TODO: work on payment Model and uncomment this
    // private CreditCard payment;

    /**
     * Validates the order. A valId order has a user, a valid associated restaurant, at least one
     * item, does not exceeded the maximum distance, and has a valid cost.
     *
     * @return true if this order is valid.
     */
    @JsonIgnore
    public boolean isValid() {
        // && !user.isEmpty() TODO: after user is created
        // user != null
        // Do we check on the restaurantId or that uuid?
        return restaurantId != null
                && this.user != null
                && this.cost != null;
    }

}
