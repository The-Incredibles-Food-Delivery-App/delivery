package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
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
        final DeliveryDriver defaultdriver1 = new DeliveryDriver();
        defaultdriver1.setFirstName("Jonny");
        defaultdriver1.setLastName("Jingleheimersmith");
        defaultdriver1.setUserName("JJS");
        defaultdriver1.setEmail("anemail@google.com");
        defaultdriver1.setPhoneNumber("55555555555");
        defaultdriver1.setCurrentlyWorking(true);

        final DeliveryDriver defaultdriver2 = new DeliveryDriver();
        defaultdriver2.setFirstName("Karen");
        defaultdriver1.setLastName("Person");
        defaultdriver1.setUserName("karen11");
        defaultdriver1.setEmail("kperson@gmail.com");
        defaultdriver1.setPhoneNumber("1112223333");
        defaultdriver1.setCurrentlyWorking(false);

        try {
            addDeliveryDriver(defaultdriver1);
            addDeliveryDriver(defaultdriver2);
        } catch (Exception e) {
            log.error("DeliveryDriverController > construct > adding default drivers > failure?");
            e.printStackTrace();
        }
    }
}
