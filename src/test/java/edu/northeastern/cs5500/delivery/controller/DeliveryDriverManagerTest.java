package edu.northeastern.cs5500.delivery.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.HashMap;
import java.util.Queue;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeliveryDriverManagerTest {
    public DeliveryDriverController deliveryDriverController;
    public DeliveryController deliveryController;
    public OrderController orderController;
    public DeliveryDriverManager deliveryDriverManager;
    public Order defaultOrder;
    public ObjectId defaultCustomer;
    public Restaurant defaultRestaurant;
    public MenuItem item1;
    public ObjectId item1Id;
    public MenuItem item2;
    public ObjectId item2Id;
    public MenuItem item3;
    public ObjectId item3Id;
    HashMap<String, Integer> items = new HashMap<>();
    public ObjectId defaultOrderId;
    Queue<DeliveryDriver> availableDriverQueue;
    public Delivery newDelivery;
    public DeliveryDriver defaultDeliveryDriver;

    @BeforeEach
    public void setup() throws InvalidOrderException, DuplicateKeyException {
        deliveryDriverController =
                new DeliveryDriverController(new InMemoryRepository<DeliveryDriver>());
        deliveryController = new DeliveryController(new InMemoryRepository<Delivery>());
        deliveryDriverManager =
                new DeliveryDriverManager(deliveryController, deliveryDriverController);

        defaultCustomer = new ObjectId();
        defaultOrder = new Order();
        item1 = new MenuItem();
        item2 = new MenuItem();
        item3 = new MenuItem();
        newDelivery = new Delivery();
        defaultDeliveryDriver = new DeliveryDriver();

        // create a valid order with three items
        item1.setName("Kimchi Soup");
        item1.setPrice(999);
        item1.setId(new ObjectId());
        item1Id = item1.getId();
        item2.setName("Bulgogi Beef");
        item2.setPrice(1299);
        item2.setId(new ObjectId());
        item2Id = item2.getId();
        item3.setName("Red Bean Mochi Cake");
        item3.setPrice(325);
        item3.setId(new ObjectId());
        item3Id = item3.getId();
        items.put(item1Id.toString(), 1);
        items.put(item2Id.toString(), 1);
        items.put(item3Id.toString(), 2);
        defaultOrder.setItems(items);

        // create a restuarant with a menu that contains those items
        defaultRestaurant = new Restaurant();
        defaultRestaurant.setRestaurantName("Seoul Cafe");
        HashMap<String, MenuItem> defaultMenu = new HashMap<>();
        defaultMenu.put(item1Id.toString(), item1);
        defaultMenu.put(item2Id.toString(), item2);
        defaultMenu.put(item3Id.toString(), item3);
        defaultRestaurant.setMenuItems(defaultMenu);
        defaultOrder.setRestaurant(defaultRestaurant);

        // create delivery driver
        defaultDeliveryDriver.setCurrentlyWorking(false);
        defaultDeliveryDriver.setFirstName("Jonny");
        defaultDeliveryDriver.setLastName("Jingleheimersmith");
        defaultDeliveryDriver.setEmail("anemail@google.com");
        defaultDeliveryDriver.setPhoneNumber("55555555555");
        defaultDeliveryDriver.setAddress("111 Apple St");
        defaultDeliveryDriver.setCurrentOrder(null);
        defaultDeliveryDriver.setCurrentlyWorking(false);

        newDelivery.setDeliveryDriver(null);

        // complete setup of the new delivery
        defaultDeliveryDriver.setCurrentOrder(defaultOrder);
        newDelivery.setDistance(33.5);
        newDelivery.setOrder(defaultOrder);

        availableDriverQueue.add(defaultDeliveryDriver);
    }

    @Test
    void fillQueue() throws DuplicateKeyException, InvalidUserException {
        availableDriverQueue.remove();
        assertEquals(null, availableDriverQueue.peek());
        deliveryDriverManager.fillQueue(defaultDeliveryDriver);
        assertEquals(newDelivery, availableDriverQueue.peek());
    }

    @Test
    void testSendForDelivery() throws Exception {
        assertEquals(null, newDelivery.getDeliveryDriver());
        assertNotEquals(DeliveryStatus.OUT_FOR_DELIVERY, newDelivery.getDeliveryStatus());
        DeliveryDriver firstInLineDriver = availableDriverQueue.peek();
        deliveryDriverManager.sendForDelivery(newDelivery);
        assertEquals(firstInLineDriver, newDelivery.getDeliveryDriver());
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, newDelivery.getDeliveryStatus());
    }

    @Test
    void completeDelivery() throws Exception {
        DeliveryDriver driverSet = newDelivery.getDeliveryDriver();
        assertNotEquals(null, newDelivery.getDeliveryDriver());
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, newDelivery.getDeliveryStatus());
        assertEquals(true, driverSet.getCurrentlyWorking());
        deliveryDriverManager.completedDelivery(newDelivery);
        assertEquals(null, newDelivery.getDeliveryDriver());
        assertEquals(false, driverSet.getCurrentlyWorking());
    }
}
