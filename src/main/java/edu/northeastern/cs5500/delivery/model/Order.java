package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.*;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Order implements Model {
    public static final Integer MAXIMUM_HOURS_ORDER_IN_ADV = 2;

    private ObjectId id;
    private String currency;
    private HashMap<String, Integer> items;
    private OrderStatus orderStatus;
    private LocalDateTime orderTime;
    private ObjectId customerId;
    private Restaurant restaurant;
    private CreditCard payment;
    private Integer cost;

    /**
     * Validates the order. A valid order has a user, a valid associated restaurant, at least one
     * item, does not exceeded the maximum distance, and has a valid cost.
     *
     * @return true if this order is valid.
     */
    @JsonIgnore
    public boolean isValid() {
        return this.customerId != null && this.restaurant != null;
    }
}
