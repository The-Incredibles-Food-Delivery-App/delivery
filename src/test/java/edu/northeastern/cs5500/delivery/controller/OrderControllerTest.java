package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// import org.junit.jupiter.api.function.Executable;
// @TestInstance(Lifecycle.PER_CLASS)
public class OrderControllerTest {
    public final Restaurant defaultRestaurant1 = new Restaurant();
    public HashMap<String, HashMap<String, Integer>> menu1 = new HashMap<>();
    public HashMap<String, Integer> dimSumItems = new HashMap<>();
    public HashMap<String, Integer> traditionalItems = new HashMap<>();
    public Customer defaultCustomer = new Customer();
    public Order neworder = new Order();
    // public HashMap<HashMap<String, Integer>, Integer> items = new HashMap<>();
    public MenuItem item1 = new MenuItem();
    public MenuItem item2 = new MenuItem();

    @BeforeEach
    public void init() {
        // create a default restaurant
        defaultRestaurant1.setRestaurantName("China Harbor");
        defaultRestaurant1.setAddress("123 Birch Lane");
        defaultRestaurant1.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant1.setHours("11-5");

        // create a Customer
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create a valid order with two items
        item1.setName("General Tso's Chicken");
        item1.setPrice(1595);
        item2.setName("Mongolian Beef");
        item2.setPrice(1999);
        // HashMap<MenuItem, Integer> items = new HashMap<>();
        // items.put(item1, 2);
        // items.put(item2, 1);

        // complete setup of the new order
        // neworder.setItems(items);
        // neworder.setCustomer(defaultCustomer);
        // neworder.setRestaurant(defaultRestaurant1);
    }

    @Test
    void testRegisterCreatesOrders() {
        // OrderController orderController = new OrderController(new InMemoryRepository<Order>());
        // assertThat(orderController.getOrders()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidOrders() {
        // OrderController orderController = new OrderController(new InMemoryRepository<Order>());

        // for (Order order : orderController.getOrders()) {
        //     assertTrue(order.isValid());
        // }
    }

    @Test
    void testCanAddOrderValidOrder() throws DuplicateKeyException, InvalidOrderException {
        // OrderController orderController = new OrderController(new InMemoryRepository<Order>());

        // // check that the order has been added to the Order repository
        // Order addedOrder = orderController.addOrder(neworder);
        // ObjectId addedOrderID = addedOrder.getId();
        // Order addedOrderInCollection = orderController.getOrder(addedOrderID);
        // ObjectId addedOrderId = addedOrderInCollection.getId();
        // assertEquals(addedOrderInCollection, orderController.getOrder(addedOrderID));
        // assertEquals(neworder.getRestaurant(), addedOrderInCollection.getRestaurant());
        // assertEquals(neworder.getCustomer(), addedOrderInCollection.getCustomer());
        // assertEquals(neworder.getItems(), addedOrderInCollection.getItems());
    }

    @Test
    void testCanUpdateOrder() throws Exception {
        // OrderController orderController = new OrderController(new InMemoryRepository<Order>());

        // // create order and add it
        // Order addedOrder = orderController.addOrder(neworder);
        // ObjectId orderID = addedOrder.getId();

        // Order orderToUpdate = orderController.getOrder(orderID);
        // MenuItem newItem = new MenuItem();
        // newItem.setName("Shrimp Dumpling");
        // newItem.setPrice(599);
        // orderToUpdate.getItems().put(newItem, 1);

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
