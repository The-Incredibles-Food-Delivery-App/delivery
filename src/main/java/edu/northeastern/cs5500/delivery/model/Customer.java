package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Customer extends User {
    HashSet<Order> orders;

    /** @return true if this customer is valid */
    @JsonIgnore
    public boolean isValid() {
        return orders != null;
    }
}
