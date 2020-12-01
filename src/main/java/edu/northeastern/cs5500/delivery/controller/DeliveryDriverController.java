package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Customer;
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

    @Nullable
    public DeliveryDriver getDeliveryDriver(@Nonnull ObjectId uuid) {
        log.debug("DeliveryDriverController > getDriver({})", uuid);
        return deliveryDrivers.get(uuid);
    }

    @Nonnull
    public Collection<DeliveryDriver> getDeliveryDrivers() {
        log.debug("DeliveryDriverController > getDrivers()");
        return deliveryDrivers.getAll();
    }

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

    public void updateDeliveryDriver(@Nonnull DeliveryDriver driver) throws Exception {
        log.debug("DeliveryDriverController > updateDriver(...)");
        deliveryDrivers.update(driver);
    }

    public void deleteDeliveryDriver(@Nonnull ObjectId id) throws Exception {
        log.debug("DeliveryDriverController > deleteDriver(...)");
        deliveryDrivers.delete(id);
    }

    /* Initializes the driver collection with drivers */
    public void initializeDrivers() {
        log.info("DeliveryDriverController > construct > adding default drivers");

        // create the Customer.
        Customer defaultCustomer = new Customer();
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create order items
        HashMap<String, Integer> items = new HashMap<>();
        MenuItem item1 = new MenuItem();
        item1.setName("Kimchi Soup");
        item1.setPrice(899);
        item1.setId(new ObjectId());
        items.put(item1.getId().toString(), 2);

        // create the order
        final Order defaultorder1 = new Order();
        defaultorder1.setOrderTime(LocalDateTime.now());
        // TODO: How to set the restaurant? Just creating dummy reataurant for now.
        defaultorder1.setRestaurant(new Restaurant());
        defaultorder1.setCustomer(defaultCustomer);
        defaultorder1.setItems(items);

        final DeliveryDriver defaultdriver1 = new DeliveryDriver();
        defaultdriver1.setFirstName("Jonny");
        defaultdriver1.setLastName("Jingleheimersmith");
        defaultdriver1.setUserName("JJS");
        defaultdriver1.setEmail("anemail@google.com");
        defaultdriver1.setPhoneNumber("55555555555");
        defaultdriver1.setAddress("111 S Apple St");
        defaultdriver1.setCurrentlyWorking(true);
        defaultdriver1.setCurrentOrder(defaultorder1);

        // final DeliveryDriver defaultdriver2 = new DeliveryDriver();
        // defaultdriver2.setFirstName("Karen");
        // defaultdriver2.setLastName("Person");
        // defaultdriver2.setUserName("karen11");
        // defaultdriver2.setEmail("kperson@gmail.com");
        // defaultdriver2.setPhoneNumber("1112223333");
        // defaultdriver2.setCurrentlyWorking(false);

        try {
            addDeliveryDriver(defaultdriver1);
            // addDeliveryDriver(defaultdriver2);
        } catch (Exception e) {
            log.error("DeliveryDriverController > construct > adding default drivers > failure?");
            e.printStackTrace();
        }
    }
}
