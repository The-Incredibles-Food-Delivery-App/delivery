package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Customer;
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
        initalizeOrders();

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
     * @throws Exception
     */
    public void updateOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > updateOrder(...)");
        // TODO: Do we need to check that the order id exists in the repo?
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
        // TODO: Do we want to throw an exception if set in the past?
        // TODO: Is this method chaining bad?
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

    /** Creates default orders to add to the order repository */
    private void initalizeOrders() {
        log.info("OrderController > construct > adding default orders");

        // create the Customer
        Customer defaultCustomer = new Customer();
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create order items
        HashMap<String, Integer> items = new HashMap<>();

        // create the order
        final Order defaultorder1 = new Order();
        defaultorder1.setOrderTime(LocalDateTime.now());
        // TODO: How to set the restaurant? Just creating dummy reataurant for now.
        defaultorder1.setRestaurant(new Restaurant());
        defaultorder1.setCustomer(defaultCustomer);
        defaultorder1.setItems(items);

        try {
            addOrder(defaultorder1);
        } catch (Exception e) {
            log.error("OrderController > construct > adding default orders > failure?");
            e.printStackTrace();
        }
    }
}
