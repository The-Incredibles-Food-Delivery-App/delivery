package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import java.util.*;

@Singleton
@Slf4j
public class OrderController {
    private final GenericRepository<Order> orders;

    @Inject
    OrderController(GenericRepository<Order> orderRepository) {
        orders = orderRepository;

        log.info("DeliveryController > construct");

        if (orders.count() > 0) {
            return;
        }

        log.info("DeliveryController > construct > adding default orders");

        final Order defaultorder1 = new Order();
        HashMap<String, Integer> item1;
        item1.put("Masala dosa", 1);

        final Order defaultorder2 = new Order();
        HashMap<String, Integer> item2;
        item2.put("Samosa", 2);


        try {
            addOrder(defaultOrder1);
            addOrder(defaultOrder2);
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
    public Order addOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > addOrder(...)");
        if (!order.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidDeliveryException");
        }

        ObjectId id = order.getId();

        if (id != null && orders.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return orders.add(order);
    }

    @Nonnull
    public Collection<Order> getOrders() {
        log.debug("OrderController > getDeliveries()");
        return;
        // return orders.getAll();
    }

    public void updateOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > updateOrder(...)");
        return;
        // deliveries.update(delivery);
    }

    public void deleteOrder(@Nonnull ObjectId id) throws Exception {
        log.debug("OrderController > deleteOrder(...)");
        return;
        // deliveries.delete(id);
    }

}