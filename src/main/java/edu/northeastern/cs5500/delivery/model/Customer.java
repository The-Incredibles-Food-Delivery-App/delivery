package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Customer extends User {
    private HashSet<Order> orders;
    private String username;
    private String password;

    /** @return true if this customer is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.username != null && this.password != null;
    }
}
