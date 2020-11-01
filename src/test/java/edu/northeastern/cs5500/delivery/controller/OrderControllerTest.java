package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.HashMap;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

// import org.junit.jupiter.api.function.Executable;

@TestInstance(Lifecycle.PER_CLASS)
public class OrderControllerTest {
    public final Restaurant defaultRestaurant1 = new Restaurant();
    public HashMap<String, HashMap<String, Integer>> menu1 = new HashMap<>();
    public HashMap<String, Integer> dimSumItems = new HashMap<>();
    public HashMap<String, Integer> traditionalItems = new HashMap<>();
    public Customer defaultCustomer = new Customer();
    public Order neworder = new Order();
    public HashMap<HashMap<String, Integer>, Integer> items = new HashMap<>();
    public HashMap<String, Integer> item1 = new HashMap<>();
    public HashMap<String, Integer> item2 = new HashMap<>();

    @BeforeAll
    public void init() {
        // create a default restaurant
        dimSumItems.put("BBQ Pork Bun", 499);
        dimSumItems.put("Shrimp Dumpling", 599);
        dimSumItems.put("Salty Dumpling with Pork", 499);
        dimSumItems.put("Sesame Ball", 499);

        traditionalItems.put("General Tso's Chicken", 1595);
        traditionalItems.put("Mongolian Beef", 1995);
        traditionalItems.put("Tripple Delight", 2095);
        traditionalItems.put("Honey Walnut Prawn", 1995);

        menu1.put("DimSum Menu", dimSumItems);
        menu1.put("Traditional Menu", traditionalItems);

        defaultRestaurant1.setRestaurantName("China Harbor");
        defaultRestaurant1.setAddress("123 Birch Lane");
        defaultRestaurant1.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant1.setHours("11-5");
        defaultRestaurant1.setPendingOrders(null);

        // create a Customer
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create a valid order with two items
        HashMap<String, Integer> item1 = new HashMap<>();
        item1.put("General Tso's Chicken", 1595);
        items.put(item1, 1);
        HashMap<String, Integer> item2 = new HashMap<>();
        item1.put("BBQ Pork Bun", 499);
        items.put(item2, 1);

        // complete setup of the new order
        neworder.setItems(items);
        neworder.setCustomer(defaultCustomer);
        neworder.setRestaurant(defaultRestaurant1);
    }

    @Test
    void testRegisterCreatesOrders() {
        OrderController orderController = new OrderController(new InMemoryRepository<Order>());
        assertThat(orderController.getOrders()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidOrders() {
        OrderController orderController = new OrderController(new InMemoryRepository<Order>());

        for (Order order : orderController.getOrders()) {
            assertTrue(order.isValid());
        }
    }

    @Test
    void testCanAddOrderValidOrder() throws DuplicateKeyException, InvalidOrderException {
        OrderController orderController = new OrderController(new InMemoryRepository<Order>());

        // check that the order has been added to the Order repository
        Order addedOrder = orderController.addOrder(neworder);
        ObjectId addedOrderID = addedOrder.getId();
        Order addedOrderInCollection = orderController.getOrder(addedOrderID);
        assertEquals(neworder.getRestaurant(), addedOrderInCollection.getRestaurant());
        assertEquals(neworder.getCustomer(), addedOrderInCollection.getCustomer());
        assertEquals(neworder.getItems(), addedOrderInCollection.getItems());
    }

    @Test
    void testCanUpdateOrder() throws Exception {
        OrderController orderController = new OrderController(new InMemoryRepository<Order>());

        // create order and add it
        Order addedOrder = orderController.addOrder(neworder);
        ObjectId orderID = addedOrder.getId();

        Order orderToUpdate = orderController.getOrder(orderID);
        HashMap<String, Integer> newItem = new HashMap<>();
        newItem.put("Shrimp Dumpling", 599);
        orderToUpdate.getItems().put(newItem, 1);

        // TODO: how do I update an order??
        // Add an item to the order and check to see if order contains additional item
        // orderController.updateOrder(orderToUpdate);
        // assertEquals(3, orderController.getOrder(orderID).getItems());
    }

    @Test
    void testCanDeleteOrder() {
        // This test should NOT call register
        // TODO: implement this test
    }

    // @Test
    // void testInvalidOrderNoCustomer() throws DuplicateKeyException, InvalidOrderException {
    //     OrderController orderController =
    //     new OrderController(new InMemoryRepository<Order>());

    // // make an invalid order and try to add it

    //     assertThrows(InvalidOrderException.class, new Executable() {
    //         @Override
    //         public void execute() throws Throwable {
    //             orderController.addOrder(neworder);
    //         }
    //     });
    // }

}
