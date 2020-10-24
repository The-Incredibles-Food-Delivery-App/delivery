package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;

@EqualsAndHashCode(callSuper = true)
@Data
public class Customer extends User {
    HashSet<Order> orders;

    /** @return true if this delivery is valid */
    @JsonIgnore
    public boolean isValid() {
        return orders != null;
    }
    
}
