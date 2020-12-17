package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DeliveryManager;
import edu.northeastern.cs5500.delivery.controller.OrderController;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderStatus;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class OrderView implements View {

    @Inject
    OrderView() {}

    @Inject JsonTransformer jsonTransformer;
    @Inject OrderController orderController;
    @Inject DeliveryManager deliveryManager;

    @Override
    public void register() {
        log.info("OrderView > register");

        get(
                "/order",
                (request, response) -> {
                    log.debug("/order");
                    response.type("application/json");
                    return orderController.getOrders();
                },
                jsonTransformer);

        get(
                "/order/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/order/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Order order = orderController.getOrder(id);
                    if (order == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return order;
                },
                jsonTransformer);

        get(
                "/calculatetotal/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/order/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Order order = orderController.getOrder(id);
                    if (order == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    orderController.calculateCost(order);
                    return orderController.getOrder(id);
                },
                jsonTransformer);

        put(
                "/additem",
                (request, response) -> {
                    final String orderParam = request.queryParams("orderId");
                    final String itemParam = request.queryParams("itemId");
                    final String quantityParam = request.queryParams("quantity");
                    log.debug("/order/additem/:itemid<{}>", itemParam);
                    final ObjectId orderId = new ObjectId(orderParam);
                    final ObjectId itemId = new ObjectId(itemParam);
                    // TODO: CSM response: Make sure frontend enforces that quantity is a valid int
                    final Integer quantity = Integer.parseInt(quantityParam);
                    Order revisedOrder = orderController.addItemToOrder(orderId, itemId, quantity);
                    if (revisedOrder == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return revisedOrder;
                },
                jsonTransformer);

        post(
                "/order",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Order order = mapper.readValue(request.body(), Order.class);
                    if (!order.isValid()) {
                        response.status(400);
                        return "";
                    }
                    // Ignore the user-provided ID if there is one
                    order.setId(null);
                    // Set the order status
                    order.setOrderStatus(OrderStatus.NOT_CONFIRMED);
                    order = orderController.addOrder(order);
                    // response.redirect(String.format("/order/{}", order.getId().toHexString(),
                    // 301));
                    return order;
                },
                jsonTransformer);

        put(
                "/submitorder",
                (request, response) -> {
                    final String orderParam = request.queryParams("orderId");
                    log.debug("/submitorder/:orderid<{}>", orderParam);
                    final ObjectId orderId = new ObjectId(orderParam);
                    Order order = orderController.getOrder(orderId);
                    deliveryManager.submitOrder(order);
                    return order;
                },
                jsonTransformer);

        put(
                "/order",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Order order = mapper.readValue(request.body(), Order.class);
                    if (!order.isValid()) {
                        response.status(400);
                        return "";
                    }
                    orderController.updateOrder(order);
                    response.type("application/json");
                    return order;
                },
                jsonTransformer);

        delete(
                "/order",
                (request, response) -> {
                    final String orderParam = request.queryParams("orderId");
                    final ObjectId orderId = new ObjectId(orderParam);
                    orderController.deleteOrder(orderId);
                    response.type("application/json");
                    // TODO: should we return something else?
                    return null;
                });
    }
}
