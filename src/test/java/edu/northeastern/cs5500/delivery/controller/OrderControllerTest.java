package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
// import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.bson.types.ObjectId;

import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;


public class OrderControllerTest {

    @Test
    void testRegisterCreatesOrders() {
        OrderController orderController =
                new OrderController(new InMemoryRepository<Order>());
        assertThat(orderController.getOrders()).isNotEmpty();
    }


    @Test
    void testRegisterCreatesValidOrders() {
        OrderController orderController =
                new OrderController(new InMemoryRepository<Order>());

        for (Order order : orderController.getOrders()) {
            assertTrue(order.isValid());
        }
    }


    @Test
    void testCanAddOrderValidOrder() throws DuplicateKeyException, InvalidOrderException {
        OrderController orderController =
        new OrderController(new InMemoryRepository<Order>());

        // create a valid order with two items
        final Order neworder = new Order();
        ArrayList<HashMap<String, Integer>> items = new ArrayList<>();
        HashMap<String, Integer> item1 = new HashMap<>();
        item1.put("palak paneer", 1);
        items.add(item1);
        HashMap<String, Integer> item2 = new HashMap<>();
        item2.put("mushroom do piyaza", 2);
        items.add(item2);
        neworder.setItems(items);
        neworder.setCost(14.99);
        LocalDateTime orderTime = LocalDateTime.of(2011, 7, 8, 11, 11, 30);
        neworder.setOrderTime(orderTime);
        ObjectId newOrderId = new ObjectId();
        neworder.setId(newOrderId);

        // check that the order has been added to the Order repository
        orderController.addOrder(neworder);
        assertEquals(neworder, orderController.getOrder(newOrderId));
    }

    
}
