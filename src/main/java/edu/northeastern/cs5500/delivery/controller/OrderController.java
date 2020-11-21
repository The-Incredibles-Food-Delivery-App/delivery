package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CuisineType;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.MenuItem;
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
    OrderController(
            GenericRepository<Order> orderRepository) {
        orders = orderRepository;

        log.info("OrderController > construct");
        initalizeOrders();

        if (orders.count() > 0) {
            return;
        }
    }

    
    @Nullable
    public Order getOrder(@Nonnull ObjectId uuid) {
        log.debug("OrderController > getOrder({})", uuid);
        return orders.get(uuid);
    }

    @Nonnull
    public Collection<Order> getOrders() {
        log.debug("OrderController > getDeliveries()");
        return orders.getAll();
    }

    public void updateOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > updateOrder(...)");
        // TODO: Do we need to check that the order id exists in the repo?
        orders.update(order);
    }

    public void deleteOrder(@Nonnull ObjectId id) throws Exception {
        log.debug("OrderController > deleteOrder(...)");
        orders.delete(id);
    }

    private boolean checkOrderValid(@Nonnull Order order) throws InvalidOrderException {
        return this.verifyOrderNonempty(order) && this.verifyOrderByTime(order);
    }

    /**
     * Verifies that the order contains at least one item.
     *
     * @param order - the order to be validated
     * @throws InvalidOrderException if the order does not contain at least one item
     * @return true if the order contains at least one item.
     */
    private boolean verifyOrderNonempty(@Nonnull Order order) {
        if (order.getItems().isEmpty()) {
            return false;
        }
        // ensure we have at least one item with quantity > 0
        HashMap<ObjectId, Integer> items = order.getItems();
        int totalItemCount = 0;
        for (ObjectId item : items.keySet()) {
            int itemQuantity = items.get(item);
            totalItemCount += itemQuantity;
        }
        if (totalItemCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies that the order contains a valid order by time.
     *
     * @param order - the order to validate
     * @throws InvalidOrderException - if order time is before the current time or more than the
     *     maximum number of hours from the current time.
     * @return true if a valid order by time has been set
     */
    private boolean verifyOrderByTime(@Nonnull Order order) throws InvalidOrderException {
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
        if (!order.isValid() || !this.checkOrderValid(order)) {
            throw new InvalidOrderException("Invalid Order");
        }

        ObjectId id = order.getId();

        if (id != null && orders.get(id) != null) {
            throw new DuplicateKeyException("Key already in use");
        }
        return orders.add(order);
    }

    private void initalizeOrders() {
        log.info("OrderController > construct > adding default orders");
        // create the restaurant
        final Restaurant defaultRestaurant1 = new Restaurant();

        defaultRestaurant1.setRestaurantName("China Harbor");
        defaultRestaurant1.setAddress("123 Birch Lane");
        defaultRestaurant1.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant1.setHours("11-5");
        defaultRestaurant1.setPhoneNumber("1234567890");

        // create the Customer
        Customer defaultCustomer = new Customer();
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create order items
        HashMap<ObjectId, Integer> items = new HashMap<>();
        final MenuItem defaultItem1 = new MenuItem();
        final MenuItem defaultItem2 = new MenuItem();
        defaultItem1.setName("BBQ Pork Bun");
        defaultItem1.setPrice(499);
        defaultItem2.setName("Shrimp Dumpling");
        defaultItem2.setPrice(599);
        try {
            ObjectId item1Id = menuItemController.addMenuItem(defaultItem1).getId();
            ObjectId item2Id = menuItemController.addMenuItem(defaultItem2).getId();
            items.put(item1Id, 1);
            items.put(item2Id, 2);
        } catch (Exception e) {
            log.error(
                    "OrderController > construct > adding default menu items to order > failure?");
            e.printStackTrace();
        }

        // create the order
        final Order defaultorder1 = new Order();
        defaultorder1.setOrderTime(LocalDateTime.now());
        defaultorder1.setRestaurant(defaultRestaurant1);
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
