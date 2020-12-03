package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderStatus;

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

    // private final OrderController orderController;

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

    // @Nonnull
    // public Delivery submitOrder(@Nonnull Order order)
    //         throws DuplicateKeyException, InvalidDeliveryException {
    //     log.debug("OrderManager > submitOrder(...)");
    //     // update order status
    //     order.setOrderStatus(OrderStatus.CONFIRMED);
    //     orderController.updateOrder(order);
    //     // create a new delivery
    //     Delivery delivery = deliveryController.createDelivery(order);
    //     return delivery;
    // }

    /**
     * Submits the given order by creating a delivery for that order
     *
     * @param order - the order to be submitted
     * @throws InvalidUserException
     * @throws InvalidDeliveryException
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
    // Put this fillQueue method in the POST reuqest of delivery driver?



    /**
     * Sends a delivery out to be delivered. Assigns a delivery driver to the delivery.
     *
     * @param delivery - the delivery to be delivered
     * @return the delivery that is enroute to the customer
     * @throws Exception
     */
    @Nonnull
    public Delivery sendForDelivery(@Nonnull Delivery delivery) throws Exception {
        // Assign delivery driver to delivery, have a  delivery driver controller
        if (availableDriverQueue.peek() != null) {
          DeliveryDriver firstInLineDriver= availableDriverQueue.remove();
          delivery.setDeliveryDriver(firstInLineDriver);
          // Does this update the current working status to true?
          deliveryDriverController.updateDeliveryDriver(firstInLineDriver);
        }
        // Once a driver is obtained, change delivery status and change delivery driver currently working status
        if (delivery.getDeliveryDriver() != null) {
          delivery.setDeliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY);
          deliveryController.updateDelivery(delivery);
        }
        return delivery;
    }
    

    // ADDITIONAL QUESTIONS:
    // Need to check where these methods are being called in other files
    // How to readd all delivery drivers that complete their delivery trip back to the queue?
}
