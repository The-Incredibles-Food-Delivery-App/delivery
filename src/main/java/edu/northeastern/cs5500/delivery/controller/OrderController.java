package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalDateTime;
import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class OrderController {
    private final GenericRepository<Order> orders;

    @Inject
    OrderController(GenericRepository<Order> orderRepository) {
        orders = orderRepository;

        log.info("OrderController > construct");

        if (orders.count() > 0) {
            return;
        }
    }

    /**
     * Returns the order corresponding to the uuid
     *
     * @param uuid - the id of the order to return
     * @return the order corresponding to the uuid
     */
    @Nullable
    public Order getOrder(@Nonnull ObjectId uuid) {
        log.debug("OrderController > getOrder({})", uuid);
        return orders.get(uuid);
    }

    /**
     * Returns all orders in the repository
     *
     * @return all orders in the repository
     */
    @Nonnull
    public Collection<Order> getOrders() {
        log.debug("OrderController > getOrders()");
        return orders.getAll();
    }

    /**
     * Updates the given order
     *
     * @param order - the updated order
     */
    public void updateOrder(@Nonnull Order order) {
        log.debug("OrderController > updateOrder(...)");
        orders.update(order);
    }

    /**
     * Deletes the given order from the repository
     *
     * @param id - the ID of the order to delete
     * @throws Exception
     */
    public void deleteOrder(@Nonnull ObjectId id) throws Exception {
        log.debug("OrderController > deleteOrder(...)");
        orders.delete(id);
    }

    /**
     * Verifies that the order contains a valid order by time.
     *
     * @param order - the order to validate
     * @throws InvalidOrderException - if order time is before the current time or more than the
     *     maximum number of hours from the current time.
     * @return true if a valid order by time has been set
     */
    private boolean verifyOrderTime(@Nonnull Order order) throws InvalidOrderException {
        // if OrderBy date/time is not set or set to the past, set to current timestamp
        if (order.getOrderTime() == null || order.getOrderTime().isBefore(LocalDateTime.now())) {
            order.setOrderTime(LocalDateTime.now());
        }
        // if orderBy data/time is set too far in advance, throw an exception
        LocalDateTime orderBy = order.getOrderTime();
        if (orderBy.isAfter(LocalDateTime.now())) {
            if (orderBy.getDayOfYear() != LocalDateTime.now().getDayOfYear()
                    || orderBy.getHour()
                            > LocalDateTime.now().getHour() + Order.MAXIMUM_HOURS_ORDER_IN_ADV) {
                throw new InvalidOrderException(
                        "Please choose an order time that is within "
                                + Order.MAXIMUM_HOURS_ORDER_IN_ADV
                                + " hours of the current time.");
            }
        }
        return true;
    }

    /**
     * Adds an order to the repository
     *
     * @param order - the order to add
     * @throws InvalidOrderException - when the order given is invalid
     * @throws DuplicateKeyException - when the order id is already contained in the collection of
     *     orders
     */
    public Order addOrder(@Nonnull Order order)
            throws InvalidOrderException, DuplicateKeyException {
        log.debug("OrderController > addOrder(...)");
        if (!order.isValid() || !this.verifyOrderTime(order)) {
            throw new InvalidOrderException("Invalid Order");
        }

        ObjectId id = order.getId();

        if (id != null && orders.get(id) != null) {
            throw new DuplicateKeyException("Key already in use");
        }
        return orders.add(order);
    }

    /**
     * Calculates the total cost of all items in the order and updates the cost of the order
     *
     * @param order - the given order
     * @return - the total cost of the order
     */
    @Nonnull
    public Integer calculateCost(@Nonnull Order order) {
        HashMap<String, Integer> items = order.getItems();
        Integer totalCost = 0;
        for (String id : items.keySet()) {
            // Add quantity * price to total cost
            Restaurant restaurant = order.getRestaurant();
            Integer itemPrice = restaurant.getMenuItems().get(id).getPrice();
            int quantity = items.get(id);
            totalCost += (itemPrice * quantity);
        }
        order.setCost(totalCost);
        updateOrder(order);
        return totalCost;
    }

    /**
     * Adds the given item with the specified quantity to the order
     *
     * @param orderId - the Id of the order to add an item to
     * @param itemId - the Id of the item to add to the order
     * @param quantity - the quantity of the item to add to the order
     * @return the order with the item added
     * @throws Exception
     */
    public Order addItemToOrder(ObjectId orderId, ObjectId itemId, Integer quantity)
            throws Exception {
        Order order = getOrder(orderId);
        order.getItems().put(itemId.toString(), quantity);
        updateOrder(order);
        return order;
    }
}
