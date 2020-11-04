package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.User;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


// CONVERT THIS TO A DRIVER
@TestInstance(Lifecycle.PER_CLASS)
public class DeliveryDriverTest {
    final Customer defaultCustomer = new Customer();
    final DeliveryDriver defaultDriver = new DeliveryDriver();
    final Customer defaultInvalidCustomer = new Customer();

    @BeforeEach
    public void init() {
        // Create a default User 1
        defaultCustomer.setFirstName("Rachel");
        defaultCustomer.setLastName("Woods");
        defaultCustomer.setPhoneNumber("2245678921");
        defaultCustomer.setUserName("Ra_wood");
        defaultCustomer.setEmail("ra_wood@hotmail.com");
        defaultCustomer.setAddress("444 Bollywood Blvd");

        // Create a Default Driver
        defaultDriver.setFirstName("Sam");
        defaultDriver.setLastName("Rockwell");
        defaultDriver.setPhoneNumber("8892134567");
        defaultDriver.setUserName("sam_rockwell666");
        defaultDriver.setEmail("theOnlySamRockwell@gmail.com");
        defaultDriver.setAddress("333 Hollywood Blvd");

        // Create a Default Invalid User
        defaultInvalidCustomer.setFirstName("Alaska");
        defaultInvalidCustomer.setLastName("Mills");
        defaultInvalidCustomer.setUserName("a_mills");
        defaultInvalidCustomer.setEmail("a_mills23@gmail.com");
    }

    @Test
    void testRegisterCreatesUsers() throws DuplicateKeyException, InvalidUserException {
        UserController userController = new UserController(new InMemoryRepository<User>());
        // userController.addUser(defaultCustomer);
        // userController.addUser(defaultDriver);
        assertThat(userController.getUsers()).isEmpty();

        User addedUser1 = userController.addUser(defaultCustomer);
        User addedUser2 = userController.addUser(defaultDriver);
        ObjectId addedUser1Id = addedUser1.getId();
        ObjectId addedUser2Id = addedUser2.getId();
        assertEquals(addedUser1.getId(), userController.getUser(addedUser1Id));
        assertThat(userController.getUsers()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidUsers() {
        UserController userController = new UserController(new InMemoryRepository<User>());

        for (User user : userController.getUsers()) {
            assertTrue(user.isValid());
        }
    }

    @Test
    void testRegisterCanAddValidUser() throws DuplicateKeyException, InvalidUserException {
        UserController userController = new UserController(new InMemoryRepository<User>());

        // Check the the user has been added to the User Repository
        User addedUser = userController.addUser(defaultCustomer);
        ObjectId addedUserId = addedUser.getId();
        User addedUserInCollection = userController.getUser(addedUserId);
        assertEquals(defaultCustomer.getAddress(), addedUserInCollection.getAddress());
        assertEquals(defaultCustomer.getUserName(), addedUserInCollection.getUserName());
        assertEquals(defaultCustomer.getEmail(), addedUserInCollection.getEmail());
        assertEquals(defaultCustomer.getLastName(), addedUserInCollection.getLastName());
        assertEquals(defaultCustomer.getFirstName(), addedUserInCollection.getFirstName());
    }

    @Test
    void testCanUpdateUser() throws Exception, DuplicateKeyException, InvalidUserException {
        UserController userController = new UserController(new InMemoryRepository<User>());

        // Creates User and adds it
        User addedUser = userController.addUser(defaultDriver);
        ObjectId userId = addedUser.getId();
        User userToUpdate = userController.getUser(userId);
        userToUpdate.setLastName("New-name");

        userController.updateUser(userToUpdate);
        assertEquals("New-name", userController.getUser(userId).getLastName());
    }

    @Test
    void testCanDeleteUser() throws Exception, DuplicateKeyException, InvalidUserException {
        UserController userController = new UserController(new InMemoryRepository<User>());
        User addedUser = userController.addUser(defaultCustomer);
        
        assertThat(userController.getUsers()).isNotEmpty();

        ObjectId userId = addedUser.getId();
        // User userToDeleter = userController.getUser(userId);
        userController.deleteUser(userId);
        assertThat(userController.getUsers()).isEmpty();
    }

    @Test
    void testInvalidUser() throws Exception, DuplicateKeyException, InvalidUserException {
        UserController userController = new UserController(new InMemoryRepository<User>());

        userController.addUser(defaultInvalidCustomer);
        assertThrows(
                InvalidUserException.class,
                () -> {
                    userController.addUser(defaultInvalidCustomer);
                });
        assertThat(userController.getUsers()).isEmpty();
    }
}