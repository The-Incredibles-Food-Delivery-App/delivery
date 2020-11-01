package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.User;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import java.util.HashSet;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class UserController {

    private final GenericRepository<User> users;

    // use these to keep track of the models that are
    // registered with UserController
    // TODO: DO WE NEED THESE?
    // private ArrayList<DeliveryDriver> drivers;
    // private ArrayList<Customer> customers;

    @Inject
    UserController(GenericRepository<User> userRepository) {
        users = userRepository;

        log.info("UserController > construct");

        if (users.count() > 0) {
            return;
        }

        log.info("UserController > construct > adding default users");

        final DeliveryDriver defaultDeliveryDriver1 = new DeliveryDriver();
        defaultDeliveryDriver1.setFirstName("Rachel");
        defaultDeliveryDriver1.setLastName("Woods");
        defaultDeliveryDriver1.setPhoneNumber("2245678921");
        defaultDeliveryDriver1.setUserName("Ra_wood");
        defaultDeliveryDriver1.setEmail("ra_wood@hotmail.com");
        // defaultDeliveryDriver1.setCurrentOrder(defaultorder1);
        defaultDeliveryDriver1.setCurrentlyWorking(true);

        final Customer defaultCustomer1 = new Customer();
        HashSet<Order> defaultCustomerOrders = new HashSet<>();
        // defaultCustomerOrders.add(defaultorder1);
        // defaultCustomerOrders.add(defaultorder2);
        defaultCustomer1.setFirstName("Sam");
        defaultCustomer1.setLastName("Rockwell");
        defaultCustomer1.setPhoneNumber("8892134567");
        defaultCustomer1.setUserName("sam_rockwell666");
        defaultCustomer1.setEmail("theOnlySamRockwell@gmail.com");
        defaultCustomer1.setAddress("333 Hollywood Blvd");
        defaultCustomer1.setOrders(defaultCustomerOrders);

        // Adding the Default Driver and Default Customer to an
        // ArrayList of each type --->
        // TODO: DO WE NEED THIS?
        // drivers = new ArrayList<DeliveryDriver>();
        // customers = new ArrayList<Customer>();
        // drivers.add(defaultDeliveryDriver1);
        // customers.add(defaultCustomer1);

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
