package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

// CONVERT THIS TO A CUSTOMER
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerControllerTest {
    public final Customer defaultCustomer1 = new Customer();
    public final Customer defaultCustomer2 = new Customer();;
    public final Customer defaultInvalidCustomer = new Customer();
    public final Restaurant defaultRestaurant = new Restaurant();
    public Order defaultCustomerOrder = new Order();
    private HashMap<String, Integer> defaultOrderItems = new HashMap<>();
    public MenuItem item1 = new MenuItem();
    public MenuItem item2 = new MenuItem();
    public HashSet<Order> customerOrderList = new HashSet<>();

    @BeforeEach
    public void init() {
        // Menu Item 1
        item1.setName("General Tso's Chicken");
        item1.setPrice(1595);
        item1.setId(new ObjectId());

        // Menu Item 2
        item2.setName("BBQ Pork Bun");
        item2.setPrice(499);
        item2.setId(new ObjectId());

        // complete setup of the new order
        ObjectId item1Id = item1.getId();
        ObjectId item2Id = item2.getId();

        // complete setup of the new order
        defaultOrderItems.put(item1Id.toString(), 1);
        defaultOrderItems.put(item2Id.toString(), 2);
        defaultCustomerOrder.setRestaurant(defaultRestaurant);
        defaultCustomerOrder.setItems(defaultOrderItems);
        customerOrderList.add(defaultCustomerOrder);

        // Create a default Customer 1
        defaultCustomer1.setFirstName("Rachel");
        defaultCustomer1.setLastName("Woods");
        defaultCustomer1.setPhoneNumber("2245678921");
        defaultCustomer1.setUserName("Ra_wood");
        defaultCustomer1.setEmail("ra_wood@hotmail.com");
        defaultCustomer1.setAddress("444 Bollywood Blvd");
        defaultCustomer1.setOrders(customerOrderList);

        // Create a default customer 2
        defaultCustomer2.setFirstName("Sam");
        defaultCustomer2.setLastName("Rockwell");
        defaultCustomer2.setPhoneNumber("8892134567");
        defaultCustomer2.setUserName("sam_rockwell666");
        defaultCustomer2.setEmail("theOnlySamRockwell@gmail.com");
        defaultCustomer2.setAddress("333 Hollywood Blvd");
        defaultCustomer2.setOrders(customerOrderList);

        // Create a Default Invalid Customer
        defaultInvalidCustomer.setFirstName("Alaska");
        defaultInvalidCustomer.setLastName("Mills");
        defaultInvalidCustomer.setUserName("a_mills");
        defaultInvalidCustomer.setEmail("a_mills23@gmail.com");
    }

    @Test
    void testRegisterCreatesCustomers() throws DuplicateKeyException, InvalidUserException {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());

        assertThat(customerController.getCustomers()).isEmpty();
        customerController.addCustomer(defaultCustomer1);
        assertThat(customerController.getCustomers()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidCustomers() {
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
        assertEquals(defaultCustomer1.getUserName(), addedCustomerInCollection.getUserName());
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
    void testInvalidUser() throws DuplicateKeyException {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());

        assertThrows(
                InvalidUserException.class,
                () -> {
                    customerController.addCustomer(defaultInvalidCustomer);
                });
    }
}
