package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class MenuItem implements Model {
    private String name;
    private Integer price;
    private String description;
    private ObjectId id;

    /**
     * Checks that the MenuItem is valid, a valid menu item has a nonnull name
     * and a non null price
     *
     * @return true if this MenuItem is valid
     */
    @JsonIgnore
    public boolean isValid() {
        return name != null && price != null;
    }
    
}
