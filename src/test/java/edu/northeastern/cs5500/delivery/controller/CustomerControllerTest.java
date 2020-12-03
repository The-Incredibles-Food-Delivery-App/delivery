package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.HashMap;
import java.util.HashSet;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// CONVERT THIS TO A CUSTOMER
// @TestInstance(Lifecycle.PER_CLASS)
public class CustomerControllerTest {
    public final Customer defaultCustomer1 = new Customer();
    public final Customer defaultCustomer2 = new Customer();;
    public final Customer defaultInvalidCustomer = new Customer();
    public final Restaurant defaultRestaurant = new Restaurant();
    public Order defaultCustomerOrder = new Order();
    HashMap<String, Integer> defaultOrderItems = new HashMap<>();
    public MenuItem item1 = new MenuItem();
    public MenuItem item2 = new MenuItem();
    public HashSet<Order> customerOrderList = new HashSet<>();
    public ObjectId itemId1;
    public ObjectId itemId2;

    @BeforeEach
    public void init() {
        defaultRestaurant.setRestaurantName("Harbor City");
        defaultRestaurant.setAddress("1st Ave");
        defaultRestaurant.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant.setHours("11-9");

        // Menu Item 1
        item1.setName("General Tso's Chicken");
        item1.setPrice(1595);
        item1.setId(new ObjectId());

        // Menu Item 2
        item2.setName("BBQ Pork Bun");
        item2.setPrice(499);
        item2.setId(new ObjectId());

        // complete setup of list of order items
        defaultOrderItems.put(item1.getId().toString(), 1);
        defaultOrderItems.put(item2.getId().toString(), 2);

        // complete setup of order
        defaultCustomerOrder.setRestaurant(defaultRestaurant);
        defaultCustomerOrder.setItems(defaultOrderItems);
        defaultCustomerOrder.setCustomerId(defaultCustomer1.getId());

        customerOrderList.add(defaultCustomerOrder);

        // Create a default Customer 1
        defaultCustomer1.setFirstName("Rachel");
        defaultCustomer1.setLastName("Woods");
        defaultCustomer1.setPhoneNumber("2245678921");
        defaultCustomer1.setUsername("Ra_wood");
        defaultCustomer1.setPassword("rawood123");
        defaultCustomer1.setEmail("ra_wood@hotmail.com");
        defaultCustomer1.setAddress("444 Bollywood Blvd");
        defaultCustomer1.setOrders(customerOrderList);

        // Create a default customer 2
        defaultCustomer2.setFirstName("Sam");
        defaultCustomer2.setLastName("Rockwell");
        defaultCustomer2.setPhoneNumber("8892134567");
        defaultCustomer2.setUsername("sam_rockwell666");
        defaultCustomer2.setPassword("samfox345");
        defaultCustomer2.setEmail("theOnlySamRockwell@gmail.com");
        defaultCustomer2.setAddress("333 Hollywood Blvd");
        defaultCustomer2.setOrders(null);

        // Create an invalid customer
        defaultInvalidCustomer.setFirstName("Ivan");
        defaultInvalidCustomer.setLastName("Invalid");
        defaultInvalidCustomer.setPhoneNumber("1234567890");
        defaultInvalidCustomer.setEmail("ivaninvalid@hotmail.com");
        defaultInvalidCustomer.setAddress("1234 Madeup Lane");
    }

    @Test
    void testRegisterCreatesCustomers() throws DuplicateKeyException, InvalidUserException {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());

        // assertThat(customerController.getCustomers()).isEmpty();
        // customerController.addCustomer(defaultCustomer1);
        assertThat(customerController.getCustomers()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidDeliveryDrivers() {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());

        for (Customer customer : customerController.getCustomers()) {
            assertTrue(customer.isValid());
        }
    }

    @Test
    void testRegisterCanAddValidCustomer() throws DuplicateKeyException, InvalidUserException {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());

        Customer addedCustomer = customerController.addCustomer(defaultCustomer1);
        ObjectId addedCustomerId = addedCustomer.getId();
        Customer addedCustomerInCollection = customerController.getCustomer(addedCustomerId);

        assertEquals(defaultCustomer1.getAddress(), addedCustomerInCollection.getAddress());
        assertEquals(defaultCustomer1.getUsername(), addedCustomerInCollection.getUsername());
        assertEquals(defaultCustomer1.getPassword(), addedCustomerInCollection.getPassword());
        assertEquals(defaultCustomer1.getEmail(), addedCustomerInCollection.getEmail());
        assertEquals(defaultCustomer1.getLastName(), addedCustomerInCollection.getLastName());
        assertEquals(defaultCustomer1.getFirstName(), addedCustomerInCollection.getFirstName());
    }

    @Test
    void testCanUpdateCustomer() throws Exception {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());

        Customer addedCustomer = customerController.addCustomer(defaultCustomer2);
        ObjectId addedCustomerId = addedCustomer.getId();

        Customer customerToUpdate = customerController.getCustomer(addedCustomerId);

        // Set a new menu item for the customer
        MenuItem item3 = new MenuItem();
        item3.setName("Kung Pao Chicken");
        item1.setPrice(1695);
        item3.setId(new ObjectId());
        ObjectId item3Id = item3.getId();

        // complete setup of the new order
        defaultOrderItems.put(item3Id.toString(), 1);
        defaultCustomerOrder.setItems(defaultOrderItems);
        customerOrderList.add(defaultCustomerOrder);
        customerToUpdate.setOrders(customerOrderList);

        customerController.updateCustomer(customerToUpdate);
        assertEquals(
                customerOrderList, customerController.getCustomer(addedCustomerId).getOrders());
    }

    @Test
    void testCanDeleteCustomer() throws Exception, DuplicateKeyException {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());
        Customer addedCustomer = customerController.addCustomer(defaultCustomer1);

        assertThat(customerController.getCustomers()).isNotEmpty();

        ObjectId addedCustomerId = addedCustomer.getId();
        customerController.deleteCustomer(addedCustomerId);
        assertNull(customerController.getCustomer(addedCustomerId));
    }

    @Test
    void testInvalidUser() throws InvalidUserException {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());

        assertThrows(
                InvalidUserException.class,
                () -> {
                    customerController.addCustomer(defaultInvalidCustomer);
                });
    }
}
