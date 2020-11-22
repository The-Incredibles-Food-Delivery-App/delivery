package edu.northeastern.cs5500.delivery.controller;

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

    @Inject
    DeliveryController(GenericRepository<Delivery> deliveryRepository) {
        deliveries = deliveryRepository;

        log.info("DeliveryController > construct");

        if (deliveries.count() > 0) {
            return;
        }

        log.info("DeliveryController > construct > adding default deliveries");
        initalizeDeliveries();
    }

    /**
     * Calculates the total cost of all items in the delivery order
     *
     * @param delivery - the given delivery
     * @return - the total cost of the delivery order
     */
    @Nonnull
    private Integer calculateCost(Delivery delivery) {
        HashMap<String, Integer> items = delivery.getOrder().getItems();
        Integer totalCost = 0;
        for (String id : items.keySet()) {
            // Add quantity * price to total cost
            // TODO: Grab menu item and read its price- is this ok??
            Restaurant restaurant = delivery.getOrder().getRestaurant();
            Integer itemPrice = restaurant.getMenuItems().get(id).getPrice();
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
        } else if (delivery.getDistance() < 0) {
            throw new InvalidDeliveryException("Delivery distance must be nonnegative");
        } else {
            return true;
        }
    }

    /**
     * Verifies that the delivery order contains at least one item.
     *
     * @param delivery - the delivery to be validated
     * @return true if the delivery order contains at least one item.
     */
    private boolean verifyOrderNonempty(@Nonnull Delivery delivery) {
        if (delivery.getOrder().getItems().isEmpty()) {
            return true;
        }
        // ensure we have at least one item with quantity > 0
        HashMap<String, Integer> items = delivery.getOrder().getItems();
        int totalItemCount = 0;
        for (String item : items.keySet()) {
            int itemQuantity = items.get(item);
            totalItemCount += itemQuantity;
        }
        if (totalItemCount > 0) {
            return true;
        } else {
            return false;
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
        if (!delivery.isValid() || !verifyDistance(delivery) || !verifyOrderNonempty(delivery)) {
            throw new InvalidDeliveryException("Invalid Delivery");
        }

        ObjectId id = delivery.getId();

        if (id != null && deliveries.get(id) != null) {
            throw new DuplicateKeyException("This delivery already exists");
        }
        return deliveries.add(delivery);
    }

    public void updateDelivery(@Nonnull Delivery deliveryToUpdate) throws Exception {
        log.debug("DeliveryController > updateDelivery(...)");
        deliveries.update(deliveryToUpdate);
    }

    public void deleteDelivery(@Nonnull ObjectId id) throws Exception {
        log.debug("DeliveryController > deleteDelivery(...)");
        deliveries.delete(id);
    }

    private void initalizeDeliveries() {
        // create items and order
        final Delivery defaultDelivery1 = new Delivery();
        // TODO: Figure out how to create default orders
        final Order defaultorder1 = new Order();
        // TODO: How to set the restaurant? Just creating dummy reataurant for now.
        Restaurant defaultRestaurant = new Restaurant();
        HashMap<String, MenuItem> defaultMenu = new HashMap<>();
        final MenuItem defaultItem1 = new MenuItem();
        final MenuItem defaultItem2 = new MenuItem();
        defaultItem1.setName("Masala Dosa");
        defaultItem1.setPrice(899);
        defaultItem1.setId(new ObjectId());
        defaultItem2.setName("Hakka Noodles");
        defaultItem2.setPrice(750);
        defaultItem2.setId(new ObjectId());
        defaultMenu.put(defaultItem1.getId().toString(), defaultItem1);
        defaultMenu.put(defaultItem2.getId().toString(), defaultItem2);
        defaultRestaurant.setRestaurantName("Dosa House");
        defaultRestaurant.setMenuItems(defaultMenu);
        defaultRestaurant.setPhoneNumber("9876543212");
        defaultRestaurant.setAddress("123 3rd Ave. Seattle WA 98017");
        defaultRestaurant.setHours("M-F 11am-11pm, Sat 12pm-1am, Sun 12pm-9pm");
        defaultorder1.setRestaurant(defaultRestaurant);

        // set default order items
        HashMap<String, Integer> items = new HashMap<>();
        items.put(defaultItem1.getId().toString(), 1);
        items.put(defaultItem2.getId().toString(), 2);
        defaultorder1.setItems(items);

        // create the Customer
        Customer defaultCustomer = new Customer();
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");
        defaultorder1.setCustomer(defaultCustomer);

        // complete setup of delivery
        defaultDelivery1.setNotes("Place in the basket on the front porch");
        defaultDelivery1.setDeliveryStatus(DeliveryStatus.ENROUTE);
        defaultDelivery1.setDistance(11.25);
        defaultDelivery1.setOrder(defaultorder1);

        try {
            addDelivery(defaultDelivery1);
        } catch (Exception e) {
            log.error("DeliveryController > construct > adding default deliveries > failure?");
            e.printStackTrace();
        }
    }
}
