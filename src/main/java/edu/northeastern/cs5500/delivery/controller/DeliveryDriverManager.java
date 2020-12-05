package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import java.util.Collection;
import java.util.Queue;
import java.util.LinkedList;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * A controller that manages the delivery driver process. A delivery driver manager has a delivery
 * controller and an delivery driver controller
 */
@Singleton
@Slf4j
public class DeliveryDriverManager {
    private final DeliveryController deliveryController;
    private final DeliveryDriverController deliveryDriverController;
    private Queue<DeliveryDriver> availableDriverQueue = null;

    @Inject
    DeliveryDriverManager(
            DeliveryController deliveryControllerInstance,
            DeliveryDriverController deliveryDriverControllerInstance) {
        deliveryController = deliveryControllerInstance;
        deliveryDriverController = deliveryDriverControllerInstance;

        if (availableDriverQueue != null) {
            return;
        }
        this.initDeliveryDrivers();
        log.info("DeliveryDriverManager > construct");
    }

    // Init method to call the fill queue, if the queue is null
    public void initDeliveryDrivers() {
        availableDriverQueue = new LinkedList<DeliveryDriver>();
        log.info("DeliveryDriverManager > construct > adding default driverdrivers");

        Collection<DeliveryDriver> allDrivers = deliveryDriverController.getDeliveryDrivers();
        for (DeliveryDriver driver : allDrivers) {
            availableDriverQueue.add(driver);
        }
    }

    /**
     * Fills the availableDriverQueue with this deliveryDriver. This method will be called when a
     * POST request occurs for the creation of a delivery driver OR When a delivery is completed,
     * will re-add the deliverydriver to this queue
     *
     * @param deliveryDriver - the deliveryDriver to be added to the queue
     * @throws InvalidUserException
     * @throws DuplicateKeyException
     */
    @Nonnull
    public void fillQueue(@Nonnull DeliveryDriver deliveryDriver)
            throws DuplicateKeyException, InvalidUserException {
        log.debug("DeliveryDriverManager > fillQueue(...)");
        // update queue with new delivery driver
        if (deliveryDriver.getCurrentlyWorking() == false) {
            availableDriverQueue.add(deliveryDriver);
            deliveryDriverController.addDeliveryDriver(deliveryDriver);
        }
    }

    /**
     * Sends a delivery out to be delivered. Assigns a delivery driver to the delivery.
     *
     * @param delivery - the delivery to be delivered
     * @return the delivery that is enroute to the customer
     * @throws Exception
     */
    @Nonnull
    public Delivery sendForDelivery(@Nonnull Delivery delivery) throws Exception {
        if (availableDriverQueue.peek() != null) {
            DeliveryDriver firstInLineDriver = availableDriverQueue.remove();
            // Assign delivery driver to delivery, and update deliverydriver status
            delivery.setDeliveryDriver(firstInLineDriver);
            firstInLineDriver.setCurrentlyWorking(true);
            deliveryDriverController.updateDeliveryDriver(firstInLineDriver);
        } else {
            log.info("There are no drivers available to send for delivery");
        }
        // Once a driver is obtained, update delivery status
        if (delivery.getDeliveryDriver() != null) {
            delivery.setDeliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY);
            deliveryController.updateDelivery(delivery);
        }
        return delivery;
    }

    // This method to be called in the delivery view, @ Danielle in PR
    /**
     * This method must be called when the delivery is complete (Maybe in Delivery Manager) When a
     * delivery is completed, update the currently working status of the driver to false, and re-add
     * into the queue
     *
     * @param delivery - the delivery that has completed delivery
     * @throws Exception
     */
    @Nonnull
    public void completedDelivery(@Nonnull Delivery delivery) throws Exception {
        DeliveryDriver driverCompletedDelivery = delivery.getDeliveryDriver();
        driverCompletedDelivery.setCurrentlyWorking(false);
        delivery.setDeliveryDriver(null);
        deliveryDriverController.updateDeliveryDriver(driverCompletedDelivery);
        deliveryController.updateDelivery(delivery);
        fillQueue(driverCompletedDelivery);
    }
}
