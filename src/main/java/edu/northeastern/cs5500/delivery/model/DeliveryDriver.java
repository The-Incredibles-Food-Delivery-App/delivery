package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryDriver extends User {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private Integer phoneNumber;
    private Order currentOrder;
    private Boolean currentlyWorking;

    /** @return true if this delivery driver is valid */
    @JsonIgnore
    public boolean isValid() {
        return currentlyWorking != null;
    }
}
