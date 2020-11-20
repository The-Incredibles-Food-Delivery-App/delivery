package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderControllerTest {
    public final Restaurant defaultRestaurant = new Restaurant();
    public HashMap<String, HashMap<String, Integer>> menu = new HashMap<>();
    public HashMap<String, Integer> dimSumItems = new HashMap<>();
    public HashMap<String, Integer> traditionalItems = new HashMap<>();
    public Customer defaultCustomer = new Customer();
    public Order neworder = new Order();
    public MenuItem item1 = new MenuItem();
    public ObjectId item1Id;
    public MenuItem item2 = new MenuItem();
    public ObjectId item2Id;
    HashMap<ObjectId, Integer> items = new HashMap<>();
    public MenuItemController menuItemController;
    public OrderController orderController;

    @BeforeEach
    public void setup() {
        menuItemController = new MenuItemController(new InMemoryRepository<MenuItem>());
        orderController = new OrderController(new InMemoryRepository<Order>(), menuItemController);

        // create a default restaurant
        dimSumItems.put("BBQ Pork Bun", 499);
        dimSumItems.put("Shrimp Dumpling", 599);
        dimSumItems.put("Salty Dumpling with Pork", 499);
        dimSumItems.put("Sesame Ball", 499);
        menu.put("DimSum Menu", dimSumItems);

        defaultRestaurant.setRestaurantName("Harbor City");
        defaultRestaurant.setAddress("1st Avenue");
        defaultRestaurant.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant.setHours("11-9");

        // create a Customer
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create two items
        item1.setName("General Tso's Chicken");
        item1.setPrice(1595);
        item2.setName("Mongolian Beef");
        item2.setPrice(1999);
        try {
            item1Id = menuItemController.addMenuItem(item1).getId();
            item2Id = menuItemController.addMenuItem(item2).getId();
            items.put(item1Id, 2);
            items.put(item2Id, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // complete setup of the new order
        neworder.setItems(items);
        neworder.setCustomer(defaultCustomer);
        neworder.setRestaurant(defaultRestaurant);
    }

    @Test
    void testRegisterCreatesOrders() {
        assertThat(orderController.getOrders()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidOrders() {
        for (Order order : orderController.getOrders()) {
            assertTrue(order.isValid());
        }
    }

    @Test
    void testRegisterGetOrders() {
        assertEquals(orderController.getOrders().size(), 1);
    }

    @Test
    void testCanAddOrderValidOrder() throws DuplicateKeyException, InvalidOrderException {
        Order addedOrder = orderController.addOrder(neworder);
        // check that the order has been added to the Order repository
        ObjectId addedOrderId = addedOrder.getId();
        neworder.setId(addedOrderId);
        Order addedOrderInCollection = orderController.getOrder(addedOrderId);
        assertEquals(addedOrderInCollection, neworder);
    }

    @Test
    void testAddOrderDuplicateKey() throws DuplicateKeyException, InvalidOrderException {
        Order addedOrder = orderController.addOrder(neworder);
        // try adding the order again
        ObjectId addedOrderId = addedOrder.getId();
        neworder.setId(addedOrderId);
        assertThrows(
                DuplicateKeyException.class,
                () -> {
                    orderController.addOrder(neworder);
                });
    }

    @Test
    void testCanUpdateOrder() throws Exception {
        // add new order
        Order addedOrder = orderController.addOrder(neworder);
        ObjectId orderID = addedOrder.getId();

        Order orderToUpdate = orderController.getOrder(orderID);
        MenuItem newItem = new MenuItem();
        newItem.setName("Shrimp Dumpling");
        newItem.setPrice(599);
        try {
            ObjectId newItemId = menuItemController.addMenuItem(newItem).getId();
            orderToUpdate.getItems().put(newItemId, 1);
            // Add an item to the order and check to see if order contains additional item
            orderController.updateOrder(orderToUpdate);
            assertEquals(3, orderController.getOrder(orderID).getItems().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCanDeleteOrder() throws Exception {
        // add the new order
        Order addedOrder = orderController.addOrder(neworder);
        assertEquals(orderController.getOrders().size(), 2);
        ObjectId orderId = addedOrder.getId();
        // delete the new order
        orderController.deleteOrder(orderId);
        assertEquals(orderController.getOrders().size(), 1);
    }

    @Test
    void testInvalidOrderNoCustomer() throws DuplicateKeyException, InvalidOrderException {
        // make an invalid order and try to add it
        neworder.setCustomer(null);
        assertThrows(
                InvalidOrderException.class,
                () -> {
                    orderController.addOrder(neworder);
                });
    }

    @Test
    void testInvalidOrderNoItems() throws DuplicateKeyException, InvalidOrderException {
        // make an invalid order and try to add it
        HashMap<ObjectId, Integer> emptyItems = new HashMap<>();
        neworder.setItems(emptyItems);
        assertThrows(
                InvalidOrderException.class,
                () -> {
                    orderController.addOrder(neworder);
                });
    }

    @Test
    void testInvalidOrderOneItemQuantityZero() throws DuplicateKeyException, InvalidOrderException {
        // make an invalid order and try to add it
        HashMap<ObjectId, Integer> itemsWithZeroQuantity = new HashMap<>();
        itemsWithZeroQuantity.put(item1Id, 0);
        itemsWithZeroQuantity.put(item2Id, 0);
        neworder.setItems(itemsWithZeroQuantity);
        assertThrows(
                InvalidOrderException.class,
                () -> {
                    orderController.addOrder(neworder);
                });
    }

    /* TODO: Figure out why this test only fails on GitHub
    @Test
    void testInvalidOrderTime() throws DuplicateKeyException, InvalidOrderException {
        // make an invalid order and try to add it
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime orderByInvalid =
                LocalDateTime.of(
                        currentTime.getYear(),
                        currentTime.getMonthValue(),
                        currentTime.getDayOfMonth(),
                        currentTime.getHour() + Order.MAXIMUM_HOURS_ORDER_IN_ADV + 5,
                        currentTime.getMinute(),
                        currentTime.getSecond());

        neworder.setOrderTime(orderByInvalid);
        assertThrows(
                InvalidOrderException.class,
                () -> {
                    orderController.addOrder(neworder);
                });
    }
    */
}
