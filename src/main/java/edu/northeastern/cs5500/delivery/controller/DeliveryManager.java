package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryStatus;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderStatus;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * A controller that manages the delivery process. A delivery manager has a delivery controller and
 * an order controller
 */
@Singleton
@Slf4j
public class DeliveryManager {
    private final DeliveryController deliveryController;
    private final OrderController orderController;

    @Inject
    DeliveryManager(
            DeliveryController deliveryControllerInstance,
            OrderController orderControllerInstance) {
        deliveryController = deliveryControllerInstance;
        orderController = orderControllerInstance;

        log.info("DeliveryManager > construct");
    }

    /**
     * Submits the given order by creating a delivery for that order
     *
     * @param order - the order to be submitted
     * @throws InvalidDeliveryException
     * @throws DuplicateKeyException
     */
    @Nonnull
    public Delivery submitOrder(@Nonnull Order order)
            throws DuplicateKeyException, InvalidDeliveryException {
        log.debug("OrderManager > submitOrder(...)");
        // update order status
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderController.updateOrder(order);
        // create a new delivery
        Delivery delivery = deliveryController.createDelivery(order);
        return delivery;
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
        // TODO: Assign delivery driver to delivery, have a  delivery driver controller?
        // Once a driver is obtained, change delivery status
        delivery.setDeliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY);
        deliveryController.updateDelivery(delivery);
        return delivery;
    }
}
