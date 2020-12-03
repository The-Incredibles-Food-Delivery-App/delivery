package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import java.util.Queue;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * A controller that manages the delivery driver process. A delivery driver manager has a delivery controller and
 * an delivery driver controller
 */
@Singleton
@Slf4j
public class DeliveryDriverManager {
    private final DeliveryController deliveryController;
    private final DeliveryDriverController deliveryDriverController;
    private Queue<DeliveryDriver> availableDriverQueue;


    @Inject
    DeliveryDriverManager(
            DeliveryController deliveryControllerInstance,
            DeliveryDriverController deliveryDriverControllerInstance,
            Queue<DeliveryDriver> availableDriverQueueInstance) {
        deliveryController = deliveryControllerInstance;
        deliveryDriverController = deliveryDriverControllerInstance;
        availableDriverQueue = availableDriverQueueInstance;

        log.info("DeliveryDriverManager > construct");
    }



    // Put this fillQueue method in the POST reuqest of delivery driver && When a delivery is completed, will re-add the deliverydriver to this queue
    /**
     * Fills the availableDriverQueue with this deliveryDriver
     *
     * @param deliveryDriver - the deliveryDriver to be added to the queue
     * @throws InvalidUserException
     * @throws DuplicateKeyException
     */
    @Nonnull
    public void fillQueue(@Nonnull DeliveryDriver deliveryDriver) throws DuplicateKeyException, InvalidUserException {
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
          DeliveryDriver firstInLineDriver= availableDriverQueue.remove();
          // Assign delivery driver to delivery, have a  delivery driver controller
          delivery.setDeliveryDriver(firstInLineDriver);
          // This individual needs the PUT request to happen on them so they FLIP their working status to true
          deliveryDriverController.updateDeliveryDriver(firstInLineDriver);
        }
        // Once a driver is obtained, change delivery status and change delivery driver currently working status
        if (delivery.getDeliveryDriver() != null) {
          delivery.setDeliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY);
          deliveryController.updateDelivery(delivery);
        }
        return delivery;
    }

    /**
     * This method must be called when the delivery is complete (Maybe in Delivery Manager)
     * When a delivery is completed, update the currently working status of the driver to false, 
     * and re-add into the queue
     *
     * @param delivery - the delivery that has completed delivery
     * @throws Exception
     */
    @Nonnull
    public void completedDelivery(@Nonnull Delivery delivery) throws Exception {
        DeliveryDriver driverCompletedDelivery = delivery.getDeliveryDriver();
        deliveryDriverController.updateDeliveryDriver(driverCompletedDelivery);
        fillQueue(driverCompletedDelivery);
    }
    

    // ADDITIONAL QUESTIONS:
    // How to re-add all delivery drivers that complete their delivery trip back to the queue?
}
