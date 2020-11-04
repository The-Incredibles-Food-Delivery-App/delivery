package edu.northeastern.cs5500.delivery.controller;

package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

// CONVERT THIS TO A DRIVER
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerControllerTest {
    final Customer defaultCustomer1 = new Customer();
    final Customer defaultCustomer2 = new Customer();;
    final Customer defaultInvalidCustomer = new Customer();
    public Order defaultCustomerOrder = new Order();
    public HashMap<HashMap<String, Integer>, Integer> items = new HashMap<>();
    public HashMap<String, Integer> item1 = new HashMap<>();
    public HashMap<String, Integer> item2 = new HashMap<>();

    @BeforeEach
    public void init() {
        // Create a default Customer 1
        defaultCustomer1.setFirstName("Rachel");
        defaultCustomer1.setLastName("Woods");
        defaultCustomer1.setPhoneNumber("2245678921");
        defaultCustomer1.setUserName("Ra_wood");
        defaultCustomer1.setEmail("ra_wood@hotmail.com");
        defaultCustomer1.setAddress("444 Bollywood Blvd");

        // Create a default customer 2
        defaultCustomer2.setFirstName("Sam");
        defaultCustomer2.setLastName("Rockwell");
        defaultCustomer2.setPhoneNumber("8892134567");
        defaultCustomer2.setUserName("sam_rockwell666");
        defaultCustomer2.setEmail("theOnlySamRockwell@gmail.com");
        defaultCustomer2.setAddress("333 Hollywood Blvd");

        // Create a Default Invalid Customer
        defaultInvalidCustomer.setFirstName("Alaska");
        defaultInvalidCustomer.setLastName("Mills");
        defaultInvalidCustomer.setUserName("a_mills");
        defaultInvalidCustomer.setEmail("a_mills23@gmail.com");
    }

    @Test
    void testRegisterCreatesDrivers() {
        CustomerController customerController = new CustomerController(new InMemoryRepository<Customer>());
        assertThat(customerController.getCustomers()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidDeliveryDrivers() {
        CustomerController customerController = new CustomerController(new InMemoryRepository<Customer>());

        for (Customer customer : customerController.getCustomers()) {
            assertTrue(customer.isValid());
        }
    }

    @Test
    void testRegisterCanAddValidDeliveryDriver()
            throws DuplicateKeyException, InvalidUserException {
                CustomerController customerController = new CustomerController(new InMemoryRepository<Customer>());

        Customer addedCustomer = customerController.addCustomer(defaultCustomer1);
        ObjectId addedCustomerId = addedCustomer.getId();
        Customer addedCustomerInCollection = customerController.getCustomer(addedCustomerId);

        assertEquals(defaultCustomer1.getAddress(), addedCustomerInCollection.getAddress());
        assertEquals(defaultCustomer1.getUserName(), addedCustomerInCollection.getUserName());
        assertEquals(defaultCustomer1.getEmail(), addedCustomerInCollection.getEmail());
        assertEquals(defaultCustomer1.getLastName(), addedCustomerInCollection.getLastName());
        assertEquals(defaultCustomer1.getFirstName(), addedCustomerInCollection.getFirstName());
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
