package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Delivery implements Model {
    public static final Integer MAXIMUM_DISTANCE = 45;

    private ObjectId id;
    private DeliveryDriver deliveryDriver;
    private DeliveryStatus deliveryStatus;
    private double distance;
    private Order order;
    private String notes;
    private LocalDateTime completionTime;

    /**
     * Checks that the delivery is valid, a valid delivery has a nonnull order.
     *
     * @return true if this delivery is valid
     */
    @JsonIgnore
    public boolean isValid() {
        return order != null;
    }
}
