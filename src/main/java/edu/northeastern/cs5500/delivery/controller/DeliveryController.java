package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DeliveryController {
    private final GenericRepository<Delivery> deliveries;
    private final MenuItemController menuItemController;

    @Inject
    DeliveryController(
            GenericRepository<Delivery> deliveryRepository,
            MenuItemController menuItemControllerInstance) {
        deliveries = deliveryRepository;
        menuItemController = menuItemControllerInstance;

        log.info("DeliveryController > construct");

        if (deliveries.count() > 0) {
            return;
        }

        log.info("DeliveryController > construct > adding default deliveries");

        // create a default restaurant
        final Restaurant defaultRestaurant1 = new Restaurant();
        HashMap<String, HashMap<String, Integer>> menu1 = new HashMap<>();
        HashMap<String, Integer> dimSumItems = new HashMap<>();
        dimSumItems.put("BBQ Pork Bun", 499);
        dimSumItems.put("Shrimp Dumpling", 599);
        dimSumItems.put("Salty Dumpliint with Pork", 499);
        dimSumItems.put("Sesame Ball", 499);

        HashMap<String, Integer> traditionalItems = new HashMap<>();
        traditionalItems.put("General Tso's Chicken", 1595);
        traditionalItems.put("Mongolian Beef", 1995);
        traditionalItems.put("Tripple Delight", 2095);
        traditionalItems.put("Honey Walnut Prawn", 1995);

        menu1.put("DimSum Menu", dimSumItems);
        menu1.put("Traditional Menu", traditionalItems);

        defaultRestaurant1.setRestaurantName("China Harbor");
        defaultRestaurant1.setAddress("123 Birch Lane");
        defaultRestaurant1.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant1.setHours("11-5");
        defaultRestaurant1.setPendingOrders(null);
        defaultRestaurant1.setPhoneNumber("1234567890");
        defaultRestaurant1.setMenu(menu1);

        // create the Customer
        Customer defaultCustomer = new Customer();
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create items and order
        final Delivery defaultDelivery1 = new Delivery();
        HashMap<MenuItem, Integer> items = new HashMap<>();
        final MenuItem defaultItem1 = new MenuItem();
        final MenuItem defaultItem2 = new MenuItem();
        final Order defaultorder1 = new Order();

        defaultItem1.setName("BBQ Pork Bun");
        defaultItem1.setPrice(499);
        items.put(defaultItem1, 1);
        defaultItem2.setName("Shrimp Dumpling");
        defaultItem2.setPrice(599);
        items.put(defaultItem2, 2);

        defaultDelivery1.setNotes("Place in the basket on the front porch");
        defaultDelivery1.setDeliveryStatus(DeliveryStatus.ENROUTE);
        defaultDelivery1.setDistance(11.25);
        defaultDelivery1.setOrder(defaultorder1);

        try {
            addDelivery(defaultDelivery1);
            // addDelivery(defaultDelivery2);
        } catch (Exception e) {
            log.error("DeliveryController > construct > adding default deliveries > failure?");
            e.printStackTrace();
        }
    }

    /**
     * Calculates the total cost of all items in the delivery order
     *
     * @param delivery - the given delivery
     * @return - the total cost of the delivery order
     */
    @Nonnull
    private Integer calculateCost(Delivery delivery) {
        HashMap<ObjectId, Integer> items = delivery.getOrder().getItems();
        Integer totalCost = 0;

        for (ObjectId id : items.keySet()) {
            // Add quantity * price to total cost
            Integer itemPrice = menuItemController.getMenuItem(id).getPrice();
            int quantity = items.get(id);
            totalCost += (itemPrice * quantity);
        }
        return totalCost;
    }

    @Nullable
    public Delivery getDelivery(@Nonnull ObjectId uuid) {
        log.debug("DeliveryController > getDelivery({})", uuid);
        return deliveries.get(uuid);
    }

    @Nonnull
    public Collection<Delivery> getDeliveries() {
        log.debug("DeliveryController > getDeliveries()");
        return deliveries.getAll();
    }

    /**
     * Validates the distance. A valid distance is less than or equal to the maximum allowed
     * distance
     *
     * @param delivery - the delivery to be validated
     * @throws InvalidDeliveryException if the delivery exceeds the max delivery distance
     * @return true if the distance is less than or equal to the max delivery distance.
     */
    public boolean verifyDistance(@Nonnull Delivery delivery) throws InvalidDeliveryException {
        if (delivery.getDistance() > Delivery.MAXIMUM_DISTANCE) {
            String message =
                    "Distance exceeds maximum allowed delivery distance"
                            + "Delivery distance cannot exceed"
                            + Delivery.MAXIMUM_DISTANCE
                            + " miles.";
            throw new InvalidDeliveryException(message);
        } else {
            return true;
        }
    }

    @Nonnull
    public Delivery addDelivery(@Nonnull Delivery delivery)
            throws DuplicateKeyException, InvalidDeliveryException {
        log.debug("DeliveryController > addDelivery(...)");

        // First compute the total cost
        Integer cost = calculateCost(delivery);
        delivery.setCost(cost);
        // verify delivery is valid
        if (!delivery.isValid() || !verifyDistance(delivery)) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new InvalidDeliveryException("Invalid Delivery");
        }

        ObjectId id = delivery.getId();

        if (id != null && deliveries.get(id) != null) {
            throw new DuplicateKeyException("This delivery already exists");
        }

        return deliveries.add(delivery);
    }

    public void updateDelivery(@Nonnull Delivery delivery) throws Exception {
        log.debug("DeliveryController > updateDelivery(...)");
        deliveries.update(delivery);
    }

    public void deleteDelivery(@Nonnull ObjectId id) throws Exception {
        log.debug("DeliveryController > deleteDelivery(...)");
        deliveries.delete(id);
    }
}
