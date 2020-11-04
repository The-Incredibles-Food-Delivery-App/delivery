package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

// CONVERT THIS TO A DRIVER
@TestInstance(Lifecycle.PER_CLASS)
public class DeliveryDriverTest {
    final DeliveryDriver defaultDriver1 = new DeliveryDriver();
    final DeliveryDriver defaultDriver2 = new DeliveryDriver();;
    final DeliveryDriver defaultInvalidDriver = new DeliveryDriver();

    @BeforeEach
    public void init() {
        // Create a default Driver 1
        defaultDriver1.setFirstName("Rachel");
        defaultDriver1.setLastName("Woods");
        defaultDriver1.setPhoneNumber("2245678921");
        defaultDriver1.setUserName("Ra_wood");
        defaultDriver1.setEmail("ra_wood@hotmail.com");
        defaultDriver1.setAddress("444 Bollywood Blvd");
        defaultDriver1.setCurrentlyWorking(true);

        // Create a Default Driver
        defaultDriver2.setFirstName("Sam");
        defaultDriver2.setLastName("Rockwell");
        defaultDriver2.setPhoneNumber("8892134567");
        defaultDriver2.setUserName("sam_rockwell666");
        defaultDriver2.setEmail("theOnlySamRockwell@gmail.com");
        defaultDriver2.setAddress("333 Hollywood Blvd");
        defaultDriver2.setCurrentlyWorking(false);

        // Create a Default Invalid User
        defaultInvalidDriver.setFirstName("Alaska");
        defaultInvalidDriver.setLastName("Mills");
        defaultInvalidDriver.setUserName("a_mills");
        defaultInvalidDriver.setEmail("a_mills23@gmail.com");
    }

    @Test
    void testRegisterCreatesDrivers() {
        DeliveryDriverController deliveryDriverController =
                new DeliveryDriverController(new InMemoryRepository<DeliveryDriver>());
        assertThat(deliveryDriverController.getDeliveryDrivers()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidDeliveryDrivers() {
        DeliveryDriverController deliveryDriverController =
                new DeliveryDriverController(new InMemoryRepository<DeliveryDriver>());

        for (DeliveryDriver driver : deliveryDriverController.getDeliveryDrivers()) {
            assertTrue(driver.isValid());
        }
    }

    @Test
    void testRegisterCanAddValidDeliveryDriver()
            throws DuplicateKeyException, InvalidUserException {
        DeliveryDriverController deliveryDriverController =
                new DeliveryDriverController(new InMemoryRepository<DeliveryDriver>());

        DeliveryDriver addedDriver = deliveryDriverController.addDeliveryDriver(defaultDriver1);
        ObjectId addedDriverId = addedDriver.getId();
        DeliveryDriver addedDriverInCollection =
                deliveryDriverController.getDeliveryDriver(addedDriverId);
        assertEquals(defaultDriver1.getAddress(), addedDriverInCollection.getAddress());
        assertEquals(defaultDriver1.getUserName(), addedDriverInCollection.getUserName());
        assertEquals(defaultDriver1.getEmail(), addedDriverInCollection.getEmail());
        assertEquals(defaultDriver1.getLastName(), addedDriverInCollection.getLastName());
        assertEquals(defaultDriver1.getFirstName(), addedDriverInCollection.getFirstName());
    }

    @Test
    void testCanUpdateDriver() throws Exception {
        DeliveryDriverController deliveryDriverController =
                new DeliveryDriverController(new InMemoryRepository<DeliveryDriver>());

        DeliveryDriver addedDriver = deliveryDriverController.addDeliveryDriver(defaultDriver1);
        ObjectId addedDriverId = addedDriver.getId();

        DeliveryDriver driverToUpdate = deliveryDriverController.getDeliveryDriver(addedDriverId);
        driverToUpdate.setCurrentlyWorking(false);

        deliveryDriverController.updateDeliveryDriver(driverToUpdate);
        assertEquals(
                false,
                deliveryDriverController.getDeliveryDriver(addedDriverId).getCurrentlyWorking());
    }

    @Test
    void testCanDeleteUser() throws Exception, DuplicateKeyException {
        DeliveryDriverController deliveryDriverController =
                new DeliveryDriverController(new InMemoryRepository<DeliveryDriver>());

        DeliveryDriver addedDriver = deliveryDriverController.addDeliveryDriver(defaultDriver2);

        assertThat(deliveryDriverController.getDeliveryDrivers()).isNotEmpty();

        ObjectId addedDriverId = addedDriver.getId();
        deliveryDriverController.deleteDeliveryDriver(addedDriverId);
        assertNull(deliveryDriverController.getDeliveryDriver(addedDriverId));
    }

    @Test
    void testInvalidUser() throws DuplicateKeyException {
        DeliveryDriverController deliveryDriverController =
                new DeliveryDriverController(new InMemoryRepository<DeliveryDriver>());

        assertThrows(
                InvalidUserException.class,
                () -> {
                    deliveryDriverController.addDeliveryDriver(defaultInvalidDriver);
                });
    }
}
