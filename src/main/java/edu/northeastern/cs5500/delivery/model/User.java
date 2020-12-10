package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public abstract class User implements Model {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

    /**
     * Validates the user. A valid user has a nonnull firstname, lastname, phonenumber, address and email.
     *
     * @return true if this user is valid.
     */
    @JsonIgnore
    public boolean isValid() {
        return firstName != null
                && lastName != null
                && phoneNumber != null
                && address != null
                && email != null;
    }
}
