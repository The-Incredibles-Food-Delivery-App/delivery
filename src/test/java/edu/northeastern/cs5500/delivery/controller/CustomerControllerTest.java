package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Order;
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
    final Customer defaultCustomer1 = new Customer();
    final Customer defaultCustomer2 = new Customer();;
    final Customer defaultInvalidCustomer = new Customer();
    public HashMap<String, HashMap<String, Integer>> menu1 = new HashMap<>();
    public HashMap<String, Integer> dimSumItems = new HashMap<>();
    public HashMap<String, Integer> traditionalItems = new HashMap<>();
    public Order defaultCustomerOrder = new Order();
    public HashMap<HashMap<String, Integer>, Integer> items = new HashMap<>();
    public HashMap<String, Integer> item1 = new HashMap<>();
    public HashMap<String, Integer> item2 = new HashMap<>();
    public HashSet<Order> customerOrderList = new HashSet<>();

    @BeforeEach
    public void init() {
        // create a default restaurant
        dimSumItems.put("BBQ Pork Bun", 499);
        traditionalItems.put("General Tso's Chicken", 1595);

        menu1.put("DimSum Menu", dimSumItems);
        menu1.put("Traditional Menu", traditionalItems);

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

        // create a valid order with two items
        HashMap<String, Integer> item1 = new HashMap<>();
        item1.put("General Tso's Chicken", 1595);
        items.put(item1, 1);
        HashMap<String, Integer> item2 = new HashMap<>();
        item1.put("BBQ Pork Bun", 499);
        items.put(item2, 1);

        // complete setup of the new order
        defaultCustomerOrder.setItems(items);
        customerOrderList.add(defaultCustomerOrder);

        defaultCustomer1.setOrders(customerOrderList);
        defaultCustomer2.setOrders(customerOrderList);
    }

    @Test
    void testRegisterCreatesCustomers() {
        CustomerController customerController =
                new CustomerController(new InMemoryRepository<Customer>());
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

        HashMap<String, Integer> item1 = new HashMap<>();
        item1.put("General Tso's Chicken", 1595);
        items.put(item1, 2);

        // complete setup of the new order
        defaultCustomerOrder.setItems(items);
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
