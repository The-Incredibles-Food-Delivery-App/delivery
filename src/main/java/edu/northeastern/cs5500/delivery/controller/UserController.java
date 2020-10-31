package edu.northeastern.cs5500.delivery.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.mongodb.lang.Nullable;

import org.bson.types.ObjectId;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.User;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class UserController {

    /// USER
    // private ObjectId id;
    // private String firstName;
    // private String lastName;
    // public String userName;
    // private String email;
    // private String phoneNumber;
    // private String address;

    /// DELIVERY DRIVER
    // private ObjectId id;
    // private String firstName;
    // private String lastName;
    // private String phoneNumber;
    // private Order currentOrder; ---> NEEDS FOR IS VALID
    // private Boolean currentlyWorking;

    /// CUSTOMER
    // HashSet<Order> orders;  ----> NEEDS FOR IS VALID

    // ORDER

    // final Order defaultorder1 = new Order();
    // ArrayList<HashMap<String, Integer>> items = new ArrayList<>();
    // HashMap<String, Integer> item1 = new HashMap<>();
    // item1.put("Masala dosa", 1);
    // items.add(item1);
    // defaultorder1.setItems(items);
    // defaultorder1.setCost(8.99);
    // defaultorder1.setOrderTime(LocalDateTime.now());


    private final GenericRepository<User> users;

    // use these to keep track of the models that are
    // registered with UserController
    // TODO: DO WE NEED THESE?
    private ArrayList<DeliveryDriver> drivers;
    private ArrayList<Customer> customers;

    @Inject
    UserController(GenericRepository<User> userRepository) {
        users = userRepository;

        log.info("UserController > construct");

        if (users.count() > 0) {
            return;
        }

        log.info("UserController > construct > adding default users");

        final Order defaultorder1 = new Order();
        ArrayList<HashMap<String, Integer>> items = new ArrayList<>();
        HashMap<String, Integer> item1 = new HashMap<>();
        item1.put("Masala dosa", 1);
        items.add(item1);
        defaultorder1.setItems(items);
        defaultorder1.setCost(8.99);
        defaultorder1.setOrderTime(LocalDateTime.now());

        final Order defaultorder2 = new Order();
        ArrayList<HashMap<String, Integer>> items2 = new ArrayList<>();
        HashMap<String, Integer> item2 = new HashMap<>();
        item2.put("Pho Bac", 1);
        items2.add(item2);
        defaultorder1.setItems(items2);
        defaultorder1.setCost(10.99);
        defaultorder1.setOrderTime(LocalDateTime.now());

        final DeliveryDriver defaultDeliveryDriver1 = new DeliveryDriver();
        defaultDeliveryDriver1.setFirstName("Rachel");
        defaultDeliveryDriver1.setLastName("Woods");
        defaultDeliveryDriver1.setPhoneNumber("2245678921");
        defaultDeliveryDriver1.setUserName("Ra_wood");
        defaultDeliveryDriver1.setCurrentOrder(defaultorder1);
        defaultDeliveryDriver1.setCurrentlyWorking(true);

        final Customer defaultCustomer1 = new Customer();
        HashSet<Order> defaultCustomerOrders = new HashSet<>();
        defaultCustomerOrders.add(defaultorder1);
        defaultCustomerOrders.add(defaultorder2);
        defaultCustomer1.setFirstName("Sam");
        defaultCustomer1.setLastName("Rockwell");
        defaultCustomer1.setPhoneNumber("8892134567");
        defaultCustomer1.setAddress("333 Hollywood Blvd");
        defaultCustomer1.setUserName("sam_rockwell666");
        defaultCustomer1.setOrders(defaultCustomerOrders);


        // Adding the Default Driver and Default Customer to an
        // ArrayList of each type ---> 
        // TODO: DO WE NEED THIS?
        drivers = new ArrayList<DeliveryDriver>();
        customers = new ArrayList<Customer>();
        drivers.add(defaultDeliveryDriver1);
        customers.add(defaultCustomer1);
        
        try {
            addUser(defaultDeliveryDriver1);
            addUser(defaultCustomer1);
        } catch (Exception e) {
            log.error("UserController > construct > adding default Users > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public User getUser(@Nonnull ObjectId uuid) {
        log.debug("UserController > getUser({})", uuid);
        return users.get(uuid);
    }

    @Nullable
    public Collection<User> getUsers() {
        log.debug("UserController > getUsers(");
        return users.getAll();
    }

    @Nonnull
    public User addUser(@Nonnull User user) throws DuplicateKeyException, InvalidUserException {
        log.debug("UserController > addUser(...)");
        if (!user.isValid()) {
            throw new InvalidUserException("Invalid User");
        }

        ObjectId id = user.getId();

        if (id != null && users.get(id) != null) {
            throw new DuplicateKeyException("This user already exists");
        }

        return users.add(user);
    }

    public void updateUser(@Nonnull User user) throws Exception {
        log.debug("UserController > updateUser(...)");
        users.update(user);
    }

    public void deleteUser(@Nonnull ObjectId id) throws Exception {
        log.debug("UserController > deleteUser(...)");
        users.delete(id);
    }
}
