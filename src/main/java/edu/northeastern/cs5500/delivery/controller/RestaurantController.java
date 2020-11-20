package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RestaurantController {
    private final GenericRepository<Restaurant> restaurants;

    @Inject
    RestaurantController(GenericRepository<Restaurant> restaurantRepository) {
        restaurants = restaurantRepository;

        log.info("RestaurantController > construct");

        if (restaurants.count() > 0) {
            return;
        }

        log.info("RestaurantController > construct > adding default restaurants");
        this.initializeRestaurants();
    }

    @Nullable
    public Restaurant getRestaurant(@Nonnull ObjectId uuid) {
        log.debug("RestaurantController > getRestaurant({})", uuid);
        return restaurants.get(uuid);
    }

    @Nonnull
    public Collection<Restaurant> getRestaurants() {
        log.debug("RestaurantController > getRestaurants()");
        return restaurants.getAll();
    }

    @Nonnull
    public Restaurant addRestaurant(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > addRestaurant(...)");
        if (!restaurant.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("Invalid Restaurant");
        }

        ObjectId id = restaurant.getId();

        if (id != null && restaurants.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }
        return restaurants.add(restaurant);
    }

    public void updateRestaurant(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > updateDelivery(...)");
        restaurants.update(restaurant);
    }

    public void deleteRestaurant(@Nonnull ObjectId id) throws Exception {
        log.debug("RestaurantController > deleteRestaurant(...)");
        restaurants.delete(id);
    }

    private void initializeRestaurants() {
        final Restaurant defaultRestaurant1 = new Restaurant();
        final Restaurant defaultRestaurant2 = new Restaurant();

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
        menu1.put(item5.getId().toString(), item5);
        menu1.put(item6.getId().toString(), item6);
        menu1.put(item7.getId().toString(), item7);
        menu1.put(item8.getId().toString(), item8);
        defaultRestaurant2.setMenuItems(menu2);

        try {
            addRestaurant(defaultRestaurant1);
            addRestaurant(defaultRestaurant2);
        } catch (Exception e) {
            log.error("Restaurant Controller > construct > adding default restaurants > failure?");
            e.printStackTrace();
        }
    }
}
