package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderStatus;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderControllerTest {
    public final Restaurant defaultRestaurant = new Restaurant();
    public Customer defaultCustomer = new Customer();
    public Order neworder = new Order();
    public MenuItem item1 = new MenuItem();
    public ObjectId item1Id;
    public MenuItem item2 = new MenuItem();
    public ObjectId item2Id;
    public HashMap<String, MenuItem> menu = new HashMap<>();
    public HashMap<String, Integer> items = new HashMap<>();
    public OrderController orderController;

    @BeforeEach
    public void setup() {
        orderController = new OrderController(new InMemoryRepository<Order>());
        defaultRestaurant.setRestaurantName("Harbor City");
        defaultRestaurant.setAddress("1st Avenue");
        defaultRestaurant.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant.setHours("11-9");

        // creating a default menu for the restaurant
        item1.setName("Kimchi Soup");
        item1.setPrice(999);
        item1.setId(new ObjectId());
        item2.setName("Bulgogi Beef");
        item2.setPrice(1299);
        item2.setId(new ObjectId());
        item1Id = new ObjectId();
        item1.setId(item1Id);
        item2Id = new ObjectId();
        item2.setId(item2Id);
        menu.put(item1Id.toString(), item1);
        menu.put(item2Id.toString(), item2);
        defaultRestaurant.setMenuItems(menu);

        // create a Customer
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create an order with two items
        items.put(item1Id.toString(), 2);
        items.put(item2Id.toString(), 1);

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
        orderToUpdate.setOrderStatus(OrderStatus.CONFIRMED);
        orderController.updateOrder(orderToUpdate);
        Order updatedOrder = orderController.getOrder(orderID);
        assertEquals(OrderStatus.CONFIRMED, updatedOrder.getOrderStatus());
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
    void testCalculateCost() throws DuplicateKeyException, InvalidOrderException {
        Order addedOrder = orderController.addOrder(neworder);
        assertEquals((Integer) 3297, orderController.calculateCost(addedOrder));
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
    void testInvalidOrderTime() throws DuplicateKeyException, InvalidOrderException {
        // make an invalid order and try to add it
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime orderByInvalid = currentTime.plusHours(Order.MAXIMUM_HOURS_ORDER_IN_ADV + 1);
        neworder.setOrderTime(orderByInvalid);
        assertThrows(
                InvalidOrderException.class,
                () -> {
                    orderController.addOrder(neworder);
                });
    }

    @Test
    public void testAddItemToOrder() throws Exception {
        // Add a new order and then proceed to add another item to it
        orderController.addOrder(neworder);
        ObjectId itemIdToAdd = new ObjectId();
        ObjectId orderId = neworder.getId();
        Integer quantity = 2;
        orderController.addItemToOrder(orderId, itemIdToAdd, quantity);
        // assert both the key and correct value (quantity) are in the order items
        assertTrue(neworder.getItems().containsKey(itemIdToAdd.toString()));
        assertEquals(quantity, neworder.getItems().get(itemIdToAdd.toString()));
    }
}
