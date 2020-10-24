package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public abstract class User implements Model {
    private ObjectId id;
    private String firstName;
    private String lastName;
    public String userName;
    private String email;
    private Integer phoneNumber;
    private String address;

    /** @return true if this delivery is valid */
    @JsonIgnore
    public boolean isValid() {
        return firstName != null
               && lastName != null
               && userName != null
               && phoneNumber != null
               && address != null
               && email != null;
    }
    
}
