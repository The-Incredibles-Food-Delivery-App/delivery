package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class DeliveryDriver implements Model {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private Integer phoneNumber;
    private Order currentOrder;
    private Boolean currentlyWorking;
    

    /** @return true if this delivery driver is valid */
    @JsonIgnore
    public boolean isValid() {
        return phoneNumber != null && firstName != null
               && lastName != null;
    }
}
