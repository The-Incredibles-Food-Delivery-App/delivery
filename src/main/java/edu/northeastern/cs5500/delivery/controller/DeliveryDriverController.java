package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalDateTime;
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
public class DeliveryDriverController {
    private final GenericRepository<DeliveryDriver> deliveryDrivers;

    @Inject
    DeliveryDriverController(GenericRepository<DeliveryDriver> deliveryDriverRepository) {
        deliveryDrivers = deliveryDriverRepository;
        log.info("DeliveryDriverController > construct");

        if (deliveryDrivers.count() > 0) {
            return;
        }

        log.info("DeliveryDriverController > construct > adding default drivers");
        this.initializeDrivers();
    }

    /**
     * Returns the delivery driver corresponding to the given id
     *
     * @param uuid - the id of the delivery driver
     * @return the delivery driver corresponding to the given id
     */
    @Nullable
    public DeliveryDriver getDeliveryDriver(@Nonnull ObjectId uuid) {
        log.debug("DeliveryDriverController > getDriver({})", uuid);
        return deliveryDrivers.get(uuid);
    }

    /**
     * Returns all delivery drivers in the repo
     *
     * @return all the delivery drivers
     */
    @Nonnull
    public Collection<DeliveryDriver> getDeliveryDrivers() {
        log.debug("DeliveryDriverController > getDrivers()");
        return deliveryDrivers.getAll();
    }

    /**
     * Adds a delivery driver to the repository after it has been validated to be a valid delivery driver
     *
     * @param driver - the delivery driver to be validated
     * @throws InvalidUserException if the delivery driver is invalid
     * @return the newly added delivery driver to the repository
     */
    @Nonnull
    public DeliveryDriver addDeliveryDriver(@Nonnull DeliveryDriver driver)
            throws DuplicateKeyException, InvalidUserException {
        log.debug("DeliveryDriverController > addDriver(...)");
        if (!driver.isValid()) {
            throw new InvalidUserException("Invalid Delivery Driver");
        }

        ObjectId id = driver.getId();

        if (id != null && deliveryDrivers.get(id) != null) {
            throw new DuplicateKeyException("This delivery already exists");
        }

        return deliveryDrivers.add(driver);
    }

    /**
     * Updates the given delivery driver
     *
     * @param driver - the delivery driver object to update
     * @throws Exception
     */
    public void updateDeliveryDriver(@Nonnull DeliveryDriver driver) throws Exception {
        log.debug("DeliveryDriverController > updateDriver(...)");
        deliveryDrivers.update(driver);
    }

    /**
     * Deletes the delivery driver corresponding to the given id
     *
     * @param id - the id corresponding to the delivery driver to delete
     * @throws Exception
     */
    public void deleteDeliveryDriver(@Nonnull ObjectId id) throws Exception {
        log.debug("DeliveryDriverController > deleteDriver(...)");
        deliveryDrivers.delete(id);
    }

    /* Initializes the driver collection with drivers */
    public void initializeDrivers() {
        log.info("DeliveryDriverController > construct > adding default drivers");

        // create the order (restaurant, menuitem)
        final Order defaultorder1 = new Order();
        defaultorder1.setOrderTime(LocalDateTime.now());
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
        defaultorder1.setCustomerId(new ObjectId());

        // Create delivery driver
        final DeliveryDriver defaultdriver1 = new DeliveryDriver();
        defaultdriver1.setFirstName("Jonny");
        defaultdriver1.setLastName("Jingleheimersmith");
        defaultdriver1.setEmail("anemail@google.com");
        defaultdriver1.setPhoneNumber("55555555555");
        defaultdriver1.setAddress("111 Apple St");
        defaultdriver1.setCurrentOrder(defaultorder1);
        defaultdriver1.setCurrentlyWorking(false);

        final DeliveryDriver defaultdriver2 = new DeliveryDriver();
        defaultdriver2.setFirstName("Karen");
        defaultdriver2.setLastName("Person");
        defaultdriver2.setEmail("kperson@gmail.com");
        defaultdriver2.setPhoneNumber("1112223333");
        defaultdriver2.setAddress("111 Apple St");
        defaultdriver2.setCurrentOrder(defaultorder1);
        defaultdriver2.setCurrentlyWorking(false);

        try {
            addDeliveryDriver(defaultdriver1);
            addDeliveryDriver(defaultdriver2);
        } catch (Exception e) {
            log.error("DeliveryDriverController > construct > adding default drivers > failure?");
            e.printStackTrace();
        }
    }
}
