package edu.northeastern.cs5500.delivery.model;

import java.util.HashSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Customer extends User {
    HashSet<Order> orders;
}
