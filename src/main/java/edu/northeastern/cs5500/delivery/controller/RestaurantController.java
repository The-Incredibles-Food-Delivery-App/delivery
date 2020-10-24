package edu.northeastern.cs5500.delivery.controller;

import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.mongodb.lang.Nullable;
import org.bson.types.ObjectId;
import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import lombok.extern.slf4j.Slf4j;

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

        final Restaurant defaultRestaurant1 = new Restaurant();
        HashMap<String, HashMap<String, Double>> menu1 = new HashMap<String, HashMap<String, Double>>();
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
        //TODO??
        // defaultRestaurant1.setId(id);
        defaultRestaurant1.setPhoneNumber("1234567890");
        defaultRestaurant1.setMenu(menu1);

        final Restaurant defaultRestaurant2 = new Restaurant();

        HashMap<String, HashMap<String, Double>> menu2 = new HashMap<String, HashMap<String, Double>>();
        HashMap<String, Double> items = new HashMap<String, Double>();
        items.put("Pho Small", 10.00);
        items.put("Pho Large", 11.00);
        items.put("Bun Bo Hue", 14.00);
        items.put("Pho Tron", 11.00);

        menu2.put("DimSum Menu", items);

        defaultRestaurant2.setRestaurantName("Pho Bac");
        defaultRestaurant2.setAddress("334 Walnut Road");
        defaultRestaurant2.setHours("5-9");
        defaultRestaurant2.setPhoneNumber("7096678546");
        defaultRestaurant2.setMenu(menu2);
        // TODO??
        // defaultRestaurant2.setId(id);

        try {
            addRestaurant(defaultRestaurant1);
            addRestaurant(defaultRestaurant2);
        } catch (Exception e) {
            log.error("Restaurant Controller > construct > adding default restaurants > failure?");
            e.printStackTrace();
        }
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
            throw new Exception("InvalidDeliveryException");
        }

        ObjectId id = restaurant.getId();

        if (id !=  null && restaurants.get(id) != null) {
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
}
