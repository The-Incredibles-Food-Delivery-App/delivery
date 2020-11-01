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

        final DeliveryDriver defaultDeliveryDriver1 = new DeliveryDriver();
        defaultDeliveryDriver1.setFirstName("Rachel");
        defaultDeliveryDriver1.setLastName("Woods");
        defaultDeliveryDriver1.setPhoneNumber("2245678921");
        defaultDeliveryDriver1.setUserName("Ra_wood");
        defaultDeliveryDriver1.setEmail("ra_wood@hotmail.com");
        defaultDeliveryDriver1.setCurrentlyWorking(true);

        final Customer defaultCustomer1 = new Customer();
        defaultCustomer1.setLastName("Rockwell");
        defaultCustomer1.setPhoneNumber("8892134567");
        defaultCustomer1.setUserName("sam_rockwell666");
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
