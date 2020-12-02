package edu.northeastern.cs5500.delivery.controller;

import static org.junit.Assert.assertEquals;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderStatus;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.HashMap;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeliveryManagerTest {
    public DeliveryController deliveryController;
    public OrderController orderController;
    public DeliveryManager deliveryManager;
    public Order defaultOrder;
    public Customer defaultCustomer;
    public Restaurant defaultRestaurant;
    public MenuItem item1;
    public ObjectId item1Id;
    public MenuItem item2;
    public ObjectId item2Id;
    public MenuItem item3;
    public ObjectId item3Id;
    HashMap<String, Integer> items = new HashMap<>();
    public ObjectId defaultOrderId;

    @BeforeEach
    public void setup() throws InvalidOrderException, DuplicateKeyException {
        deliveryController = new DeliveryController(new InMemoryRepository<Delivery>());
        orderController = new OrderController(new InMemoryRepository<Order>());
        deliveryManager = new DeliveryManager(deliveryController, orderController);

        defaultCustomer = new Customer();
        defaultOrder = new Order();
        item1 = new MenuItem();
        item2 = new MenuItem();
        item3 = new MenuItem();

        // create a Customer
        defaultCustomer.setUserName("doglover26");
        defaultCustomer.setFirstName("Sanket");
        defaultCustomer.setLastName("Tekans");
        defaultCustomer.setEmail("dogsrock@gmail.com");
        defaultOrder.setCustomer(defaultCustomer);

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

        // add the order to the order repository
        try {
            defaultOrderId = orderController.addOrder(defaultOrder).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSubmitOrder() throws DuplicateKeyException, InvalidDeliveryException {
        Delivery newDelivery = deliveryManager.submitOrder(defaultOrder);
        ObjectId newDeliveryId = newDelivery.getId();
        // assert that a delivery associated with the submitted order was created
        assertEquals(newDelivery, deliveryController.getDelivery(newDeliveryId));
        Order orderSubmitted = orderController.getOrder(defaultOrderId);
        assertEquals(OrderStatus.CONFIRMED, orderSubmitted.getOrderStatus());
    }

    @Test
    void testSendForDelivery() throws Exception {
        Delivery delivery = deliveryManager.submitOrder(defaultOrder);
        ObjectId deliveryId = delivery.getId();
        // assert that a delivery associated with the submitted order was created
        deliveryManager.sendForDelivery(delivery);
        Delivery sentDelivery = deliveryController.getDelivery(deliveryId);
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, sentDelivery.getDeliveryStatus());
    }
}
