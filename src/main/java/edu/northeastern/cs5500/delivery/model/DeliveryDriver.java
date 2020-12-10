package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryDriver extends User {
    private Order currentOrder;
    private Boolean currentlyWorking;

/**
     * Checks that the deliverydriver is valid, a valid deliverydriver has a nonnull currentlyworking status.
     *
     * @return true if this deliverydriver is valid
     */    @JsonIgnore
    public boolean isValid() {
        return this.currentlyWorking != null;
    }
}
