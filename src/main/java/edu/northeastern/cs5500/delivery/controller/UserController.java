package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.User;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class UserController {

    private final GenericRepository<User> users;

    @Inject
    UserController(GenericRepository<User> userRepository) {
        users = userRepository;

        log.info("UserController > construct");

        if (users.count() > 0) {
            return;
        }

        log.info("UserController > construct > adding default users");
        this.initializeUsers();
    }

    /**
     * Returns the user corresponding to the given id
     *
     * @param uuid - the id of the user
     * @return the user corresponding to the given id
     */
    @Nullable
    public User getUser(@Nonnull ObjectId uuid) {
        log.debug("UserController > getUser({})", uuid);
        return users.get(uuid);
    }

    /**
     * Returns all users in the user repository
     *
     * @return all users
     */
    @Nullable
    public Collection<User> getUsers() {
        log.debug("UserController > getUsers(");
        return users.getAll();
    }

    /**
     * Adds a user to the repository
     *
     * @param item - the user to add
     * @throws InvalidUserException - when the user given is invalid
     * @throws DuplicateKeyException - when the item id is already contained in the collection of
     *     users
     */
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

    /**
     * Updates the given user
     *
     * @param item - the user object to update
     * @throws Exception
     */
    public void updateUser(@Nonnull User user) throws Exception {
        log.debug("UserController > updateUser(...)");
        users.update(user);
    }

    /**
     * Deletes the user corresponding to the given id
     *
     * @param id - the id corresponding to the user to delete
     * @throws Exception
     */
    public void deleteUser(@Nonnull ObjectId id) throws Exception {
        log.debug("UserController > deleteUser(...)");
        users.delete(id);
    }

    /* Initalizes the user collection with restaurants */
    private void initializeUsers() {
        final DeliveryDriver defaultDeliveryDriver1 = new DeliveryDriver();
        defaultDeliveryDriver1.setFirstName("Rachel");
        defaultDeliveryDriver1.setLastName("Woods");
        defaultDeliveryDriver1.setPhoneNumber("2245678921");
        defaultDeliveryDriver1.setEmail("ra_wood@hotmail.com");
        defaultDeliveryDriver1.setAddress("344 Wood Lane");
        defaultDeliveryDriver1.setCurrentlyWorking(false);

        final Customer defaultCustomer1 = new Customer();
        defaultCustomer1.setFirstName("Sam");
        defaultCustomer1.setLastName("Rockwell");
        defaultCustomer1.setPhoneNumber("8892134567");
        defaultCustomer1.setUsername("sam_rockwell666");
        defaultCustomer1.setPassword("samrockx666");
        defaultCustomer1.setEmail("theOnlySamRockwell@gmail.com");
        defaultCustomer1.setAddress("333 Hollywood Blvd");

        try {
            addUser(defaultDeliveryDriver1);
            addUser(defaultCustomer1);
        } catch (Exception e) {
            log.error("UserController > construct > adding default Users > failure?");
            e.printStackTrace();
        }
    }
}
