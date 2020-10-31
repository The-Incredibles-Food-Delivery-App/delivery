package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryDriver extends User {
    private Order currentOrder;
    private Boolean currentlyWorking;

    /** @return true if this delivery driver is valid */
    @JsonIgnore
    public boolean isValid() {
        return currentlyWorking != null;
    }
}
