package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CuisineType;
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

// import java.util.Collection;

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

        log.info("OrderController > construct > adding default orders");

        // create the restaurant
        final Restaurant defaultRestaurant1 = new Restaurant();
        HashMap<String, HashMap<String, Integer>> menu1 = new HashMap<>();
        HashMap<String, Integer> dimSumItems = new HashMap<>();
        dimSumItems.put("BBQ Pork Bun", 499);
        dimSumItems.put("Shrimp Dumpling", 599);
        dimSumItems.put("Salty Dumpliint with Pork", 499);
        dimSumItems.put("Sesame Ball", 499);

        HashMap<String, Integer> traditionalItems = new HashMap<>();
        traditionalItems.put("General Tso's Chicken", 1595);
        traditionalItems.put("Mongolian Beef", 1995);
        traditionalItems.put("Tripple Delight", 2095);
        traditionalItems.put("Honey Walnut Prawn", 1995);

        menu1.put("DimSum Menu", dimSumItems);
        menu1.put("Traditional Menu", traditionalItems);

        defaultRestaurant1.setRestaurantName("China Harbor");
        defaultRestaurant1.setAddress("123 Birch Lane");
        defaultRestaurant1.setCuisineType(CuisineType.CHINESE);
        defaultRestaurant1.setHours("11-5");
        defaultRestaurant1.setPendingOrders(null);
        defaultRestaurant1.setPhoneNumber("1234567890");
        defaultRestaurant1.setMenu(menu1);

        // create the Customer
        Customer defaultCustomer = new Customer();
        defaultCustomer.setUserName("catlover11");
        defaultCustomer.setFirstName("Ellie");
        defaultCustomer.setLastName("Gato");
        defaultCustomer.setEmail("gatolover@gmail.com");

        // create the order
        final Order defaultorder1 = new Order();
        HashMap<HashMap<String, Integer>, Integer> items = new HashMap<>();
        HashMap<String, Integer> item1 = new HashMap<>();
        item1.put("Pho Small", 1000);
        items.put(item1, 2);
        defaultorder1.setItems(items);
        defaultorder1.setOrderTime(LocalDateTime.now());
        defaultorder1.setRestaurant(defaultRestaurant1);
        defaultorder1.setCustomer(defaultCustomer);

        try {
            addOrder(defaultorder1);
        } catch (Exception e) {
            log.error("OrderController > construct > adding default orders > failure?");
            e.printStackTrace();
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
        // TODO: is this chaining of two methods bad? Should I add a
        // method isEmpty in the order class iteself?
        if (order.getItems().isEmpty()) {
            return false;
        }
        // If one item in the order, ensure quantity is at least 1
        if (order.getItems().size() == 1) {
            HashMap<HashMap<String, Integer>, Integer> items = order.getItems();
            for (HashMap<String, Integer> item : items.keySet()) {
                if (items.get(item) < 1) {
                    return false;
                }
            }
        }
        return true;
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
        // QUESTION: Do we want to throw an exception if set in the past?
        // QUESTION: Is this method chaining bad?
        if (order.getOrderBy() == null || order.getOrderBy().isBefore(LocalDateTime.now())) {
            order.setOrderBy(LocalDateTime.now());
        }
        // if orderBy data/time is set too far in advance, throw an exception
        LocalDateTime orderBy = order.getOrderBy();
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
}
