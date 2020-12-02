package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRestaurantRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantControllerTest {
    public RestaurantController restaurantController;
    public final Restaurant newRestaurant = new Restaurant();
    public HashMap<String, MenuItem> menu = new HashMap<>();
    public List<MenuItem> menuItemsList = new ArrayList<>();
    public MenuItem item1 = new MenuItem();
    public MenuItem item2 = new MenuItem();
    public MenuItem item3 = new MenuItem();
    public String newRestaurantName;

    @BeforeEach
    public void setup() {
        restaurantController = new RestaurantController(new InMemoryRestaurantRepository());
        // create menu items
        item1.setName("Salmon Piroshky");
        item1.setPrice(899);
        item1.setId(new ObjectId());
        item2.setName("Potato and Onion Piroshky");
        item2.setPrice(799);
        item2.setId(new ObjectId());
        item3.setName("Beef and Onion Piroshky");
        item3.setPrice(850);
        item3.setId(new ObjectId());
        menu.put(item1.getId().toString(), item1);
        menu.put(item2.getId().toString(), item2);
        menu.put(item3.getId().toString(), item3);
        menuItemsList.add(item1);
        menuItemsList.add(item2);
        menuItemsList.add(item3);
        // finish restuarant setup
        newRestaurantName = "Piroshky Piroshky";
        newRestaurant.setRestaurantName(newRestaurantName);
        newRestaurant.setPhoneNumber("9876543212");
        newRestaurant.setAddress("123 3rd Ave. Seattle WA 98017");
        newRestaurant.setHours("M-F 11am-11pm, Sat 12pm-1am, Sun 12pm-9pm");
        newRestaurant.setMenuItems(menu);
    }

    @Test
    void testRegisterCreatesRestaurants() {
        assertThat(restaurantController.getRestaurants()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidRestaurants() {
        for (Restaurant restaurant : restaurantController.getRestaurants()) {
            assertTrue(restaurant.isValid());
        }
    }

    @Test
    void testRegisterGetRestaurants() {
        // Repository should have restaurants that are loaded in the controller init method
        assertTrue(restaurantController.getRestaurants().size() > 0);
    }

    @Test
    void testCanAddRestaurantValidRestaurant()
            throws DuplicateKeyException, InvalidObjectException {
        Restaurant addedRestaurant = restaurantController.addRestaurant(newRestaurant);
        // check that the restaurant has been added to the Restaurant repository
        ObjectId addedRestaurantId = addedRestaurant.getId();
        newRestaurant.setId(addedRestaurantId);
        Restaurant addedRestaurantInCollection =
                restaurantController.getRestaurant(addedRestaurantId);
        assertEquals(addedRestaurantInCollection, newRestaurant);
    }

    @Test
    void testGetRestaurantsByName() throws DuplicateKeyException, InvalidObjectException {
        restaurantController.addRestaurant(newRestaurant);
        Collection<Restaurant> results =
                restaurantController.getRestaurantsByName(newRestaurantName);
        assertTrue(results.size() == 1);
        for (Restaurant result : results) {
            assertEquals(newRestaurantName, result.getRestaurantName());
        }
    }

    @Test
    void testAddRestaurantDuplicateKey() throws DuplicateKeyException, InvalidObjectException {
        Restaurant addedRestaurant = restaurantController.addRestaurant(newRestaurant);
        // try adding the restaurant again
        ObjectId addedRestaurantId = addedRestaurant.getId();
        newRestaurant.setId(addedRestaurantId);
        assertThrows(
                DuplicateKeyException.class,
                () -> {
                    restaurantController.addRestaurant(newRestaurant);
                });
    }

    @Test
    void testCanUpdateRestaurant() throws Exception {
        // add new restaurant
        Restaurant addedRestaurant = restaurantController.addRestaurant(newRestaurant);
        ObjectId restaurantID = addedRestaurant.getId();

        Restaurant restaurantToUpdate = restaurantController.getRestaurant(restaurantID);
        // Update the restaurant hours
        restaurantToUpdate.setHours("M-Sun 12pm-1am");
        restaurantController.updateRestaurant(restaurantToUpdate);
        assertEquals("M-Sun 12pm-1am", restaurantController.getRestaurant(restaurantID).getHours());
    }

    @Test
    void testGetMenuItems() throws Exception {
        List<MenuItem> results = restaurantController.getMenuItems(newRestaurant);
        for (MenuItem item : results) {
            assertTrue(menuItemsList.contains(item));
        }
    }

    @Test
    void testInvalidRestaurantNoMenu() throws DuplicateKeyException, InvalidObjectException {
        // make an invalid restaurant and try to add it
        newRestaurant.setMenuItems(null);
        assertThrows(
                InvalidObjectException.class,
                () -> {
                    restaurantController.addRestaurant(newRestaurant);
                });
    }

    @Test
    void testInvalidRestaurantNoHours() throws DuplicateKeyException, InvalidObjectException {
        // make an invalid restaurant and try to add it
        newRestaurant.setHours(null);
        assertThrows(
                InvalidObjectException.class,
                () -> {
                    restaurantController.addRestaurant(newRestaurant);
                });
    }

    @Test
    void testInvalidRestaurantNoAddress() throws DuplicateKeyException, InvalidObjectException {
        // make an invalid restaurant and try to add it
        newRestaurant.setAddress(null);
        assertThrows(
                InvalidObjectException.class,
                () -> {
                    restaurantController.addRestaurant(newRestaurant);
                });
    }

    @Test
    void testInvalidRestaurantNoPhoneNumber() throws DuplicateKeyException, InvalidObjectException {
        // make an invalid restaurant and try to add it
        newRestaurant.setPhoneNumber(null);
        assertThrows(
                InvalidObjectException.class,
                () -> {
                    restaurantController.addRestaurant(newRestaurant);
                });
    }

    @Test
    void testCanDeleteRestaurant() throws Exception {
        // add the new restaurant
        Restaurant addedRestaurant = restaurantController.addRestaurant(newRestaurant);
        int currentSize = restaurantController.getRestaurants().size();
        ObjectId restaurantId = addedRestaurant.getId();
        // delete the new order
        restaurantController.deleteRestaurant(restaurantId);
        assertEquals(currentSize - 1, restaurantController.getRestaurants().size());
    }
}
