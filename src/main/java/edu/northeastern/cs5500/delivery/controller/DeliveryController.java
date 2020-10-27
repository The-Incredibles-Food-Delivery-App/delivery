package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        final Delivery defaultDelivery1 = new Delivery();
        defaultDelivery1.setDistance(1.25);
        Order defaultOrder1 = new Order();
        ArrayList<HashMap<String, Integer>> items = new ArrayList<>();
        HashMap<String, Integer> item1 = new HashMap<>();
        item1.put("Masala dosa", 1);
        items.add(item1);
        defaultOrder1.setItems(items);
        defaultOrder1.setCost(8.99);
        defaultOrder1.setOrderTime(LocalDateTime.now());
        defaultDelivery1.setOrder(defaultOrder1);
        defaultDelivery1.setNotes("Place in the basket on the front porch");
        defaultDelivery1.setDeliveryStatus(DeliveryStatus.ENROUTE);

        // final Delivery defaultDelivery2 = new Delivery();
        // defaultDelivery2.setTitle("A steak");
        // defaultDelivery2.setDescription("Not a hot dog");
        // defaultDelivery2.setDistance(21.0);

        try {
            addDelivery(defaultDelivery1);
            // addDelivery(defaultDelivery2);
        } catch (Exception e) {
            log.error("DeliveryController > construct > adding default deliveries > failure?");
            e.printStackTrace();
        }
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
