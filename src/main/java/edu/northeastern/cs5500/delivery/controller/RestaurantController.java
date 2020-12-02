package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRestaurantRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RestaurantController {
    private final GenericRestaurantRepository restaurants;

    @Inject
    RestaurantController(GenericRestaurantRepository restaurantRepository) {
        restaurants = restaurantRepository;

        log.info("RestaurantController > construct");

        if (restaurants.count() > 0) {
            return;
        }

        log.info("RestaurantController > construct > adding default restaurants");
        this.initializeRestaurants();
    }

    /**
     * Returns the restaurants with the given name
     *
     * @param name - the restaurant name to search for
     * @return the restaurants with the given name
     */
    public Collection<Restaurant> getRestaurantsByName(String name) {
        return restaurants.getRestaurantByName(name);
    }

    /**
     * Returns the restaurnt with the given Id
     *
     * @param uuid - the restaurant Id
     * @return the restaurnt with the given Id
     */
    @Nullable
    public Restaurant getRestaurant(@Nonnull ObjectId uuid) {
        log.debug("RestaurantController > getRestaurant({})", uuid);
        return restaurants.get(uuid);
    }

    /**
     * Returns all restaurants in the restaurant collection
     *
     * @return all restaurants in the restaurant collection
     */
    @Nonnull
    public Collection<Restaurant> getRestaurants() {
        log.debug("RestaurantController > getRestaurants()");
        return restaurants.getAll();
    }

    /**
     * Adds the given restaurant to the restaurant collection
     *
     * @param restaurant - the restaurant to add
     * @return the restaurant that was added to the collection
     * @throws DuplicateKeyException - when the restaurant id already corresponds to a restaurant in
     *     the collection
     * @throws InvalidObjectException - when an invalid restaurant is given
     */
    @Nonnull
    public Restaurant addRestaurant(@Nonnull Restaurant restaurant)
            throws DuplicateKeyException, InvalidObjectException {
        log.debug("RestaurantController > addRestaurant(...)");
        if (!restaurant.isValid()) {
            throw new InvalidObjectException("Invalid Restaurant");
        }

        ObjectId id = restaurant.getId();

        if (id != null && restaurants.get(id) != null) {
            throw new DuplicateKeyException("DuplicateKeyException");
        }
        return restaurants.add(restaurant);
    }

    /**
     * Returns all menu items for the given restaurant
     *
     * @param restaurant - the restaurant
     * @return all menu items for the given restaurant
     * @throws Exception
     */
    public List<MenuItem> getMenuItems(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > gettingMenuItems(...)");
        List<MenuItem> results = new ArrayList<>();
        for (MenuItem item : restaurant.getMenuItems().values()) {
            results.add(item);
        }
        return results;
    }

    /**
     * Updates the given restaurant
     *
     * @param restaurant - the updated restaurant
     * @throws Exception
     */
    public void updateRestaurant(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > updateDelivery(...)");
        restaurants.update(restaurant);
    }

    /**
     * Deletes the restaurant corresponding to the given id
     *
     * @param id - the id of the restaurant to be deleted
     * @throws Exception
     */
    public void deleteRestaurant(@Nonnull ObjectId id) throws Exception {
        log.debug("RestaurantController > deleteRestaurant(...)");
        restaurants.delete(id);
    }

    /** Initalizes the restaurant collection with restaurants */
    private void initializeRestaurants() {
        final Restaurant defaultRestaurant1 = new Restaurant();
        final Restaurant defaultRestaurant2 = new Restaurant();
        final Restaurant defaultRestaurant3 = new Restaurant();

        defaultRestaurant1.setRestaurantName("China Harbor");
        defaultRestaurant1.setAddress("123 Birch Lane");
        defaultRestaurant1.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant1.setHours("11-5");
        defaultRestaurant1.setPhoneNumber("1234567890");

        // create menu items and menu
        HashMap<String, MenuItem> menu1 = new HashMap<>();
        MenuItem item1 = new MenuItem();
        MenuItem item2 = new MenuItem();
        MenuItem item3 = new MenuItem();
        MenuItem item4 = new MenuItem();
        item1.setName("General Tso's Chicken");
        item1.setPrice(1595);
        item1.setId(new ObjectId());
        item2.setName("Mongolian Beef");
        item2.setPrice(1495);
        item2.setId(new ObjectId());
        item3.setName("BBQ Pork Bun");
        item3.setPrice(499);
        item3.setId(new ObjectId());
        item4.setName("Shrimp Dumpling");
        item4.setPrice(599);
        item4.setId(new ObjectId());
        menu1.put(item1.getId().toString(), item1);
        menu1.put(item2.getId().toString(), item2);
        menu1.put(item3.getId().toString(), item3);
        menu1.put(item4.getId().toString(), item4);
        defaultRestaurant1.setMenuItems(menu1);

        defaultRestaurant2.setRestaurantName("Dosa House");
        defaultRestaurant2.setAddress("4321 Pine Ave.");
        defaultRestaurant2.setCuisineType(CuisineType.INDIAN);
        defaultRestaurant2.setHours("11-11");
        defaultRestaurant2.setPhoneNumber("9876543210");

        // create menu items and menu
        HashMap<String, MenuItem> menu2 = new HashMap<>();
        MenuItem item5 = new MenuItem();
        MenuItem item6 = new MenuItem();
        MenuItem item7 = new MenuItem();
        MenuItem item8 = new MenuItem();
        item5.setName("Masala Dosa");
        item5.setPrice(899);
        item5.setId(new ObjectId());
        item6.setName("Upma Dosa");
        item6.setPrice(850);
        item6.setId(new ObjectId());
        item7.setName("Samosa");
        item7.setPrice(325);
        item7.setId(new ObjectId());
        item8.setName("Hakka Noodles");
        item8.setPrice(799);
        item8.setId(new ObjectId());
        menu2.put(item5.getId().toString(), item5);
        menu2.put(item6.getId().toString(), item6);
        menu2.put(item7.getId().toString(), item7);
        menu2.put(item8.getId().toString(), item8);
        defaultRestaurant2.setMenuItems(menu2);

        defaultRestaurant3.setRestaurantName("Queen Sheeba");
        defaultRestaurant3.setAddress("1000 Broadway Ave., Seattle, WA 98017");
        defaultRestaurant3.setCuisineType(CuisineType.AFRICAN);
        defaultRestaurant3.setHours("M-Sat 4pm-11pm, Sun 4pm-10pm");
        defaultRestaurant3.setPhoneNumber("1231231234");

        // create menu items and menu
        HashMap<String, MenuItem> menu3 = new HashMap<>();
        MenuItem item9 = new MenuItem();
        MenuItem item10 = new MenuItem();
        MenuItem item11 = new MenuItem();
        MenuItem item12 = new MenuItem();
        item9.setName("Doro Wat");
        item9.setPrice(999);
        item9.setId(new ObjectId());
        item10.setName("Chicken Tibbs");
        item10.setPrice(1099);
        item10.setId(new ObjectId());
        item11.setName("Bamia Beef");
        item11.setPrice(1050);
        item11.setId(new ObjectId());
        item12.setName("Fish Tibbs");
        item12.setPrice(1199);
        item12.setId(new ObjectId());
        menu3.put(item9.getId().toString(), item9);
        menu3.put(item10.getId().toString(), item10);
        menu3.put(item11.getId().toString(), item11);
        menu3.put(item12.getId().toString(), item12);
        defaultRestaurant3.setMenuItems(menu3);

        try {
            addRestaurant(defaultRestaurant1);
            addRestaurant(defaultRestaurant2);
            addRestaurant(defaultRestaurant3);
        } catch (Exception e) {
            log.error("Restaurant Controller > construct > adding default restaurants > failure?");
            e.printStackTrace();
        }
    }
}
