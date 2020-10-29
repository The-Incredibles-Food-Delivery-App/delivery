package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.model.User;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

// import static com.google.common.truth.Truth.assertWithMessage;

public class OrderControllerTest {

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
        OrderController orderController =
        new OrderController(new InMemoryRepository<Order>());

         // create a default restaurant
         final Restaurant defaultRestaurant1 = new Restaurant();
         HashMap<String, HashMap<String, Double>> menu1 =
                 new HashMap<String, HashMap<String, Double>>();
         HashMap<String, Double> dimSumItems = new HashMap<String, Double>();
         dimSumItems.put("BBQ Pork Bun", 4.99);
         dimSumItems.put("Shrimp Dumpling", 5.99);
         dimSumItems.put("Salty Dumpliint with Pork", 4.99);
         dimSumItems.put("Sesame Ball", 4.99);
 
         HashMap<String, Double> traditionalItems = new HashMap<String, Double>();
         traditionalItems.put("General Tso's Chicken", 15.95);
         traditionalItems.put("Mongolian Beef", 19.95);
         traditionalItems.put("Tripple Delight", 20.95);
         traditionalItems.put("Honey Walnut Prawn", 19.95);
 
         menu1.put("DimSum Menu", dimSumItems);
         menu1.put("Traditional Menu", traditionalItems);
 
         defaultRestaurant1.setRestaurantName("China Harbor");
         defaultRestaurant1.setAddress("123 Birch Lane");
         defaultRestaurant1.setCuisineType(CuisineType.CHINESE);
         defaultRestaurant1.setHours("11-5");
         defaultRestaurant1.setPendingOrders(null);
 
         // create the Customer
         User defaultCustomer = new Customer();
         defaultCustomer.setUserName("catlover11");
         defaultCustomer.setFirstName("Ellie");
         defaultCustomer.setLastName("Gato");
         defaultCustomer.setEmail("gatolover@gmail.com");
 

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
        neworder.setUser(defaultCustomer);
        neworder.setRestaurant(defaultRestaurant1);

        // check that the order has been added to the Order repository
        Order addedOrder = orderController.addOrder(neworder);
        assertEquals(neworder.getRestaurant(), addedOrder.getRestaurant());
        assertEquals(neworder.getUser(), addedOrder.getUser());
        assertEquals(neworder.getItems(), addedOrder.getItems());
        assertEquals(neworder.getCost(), addedOrder.getCost());
    }

}
