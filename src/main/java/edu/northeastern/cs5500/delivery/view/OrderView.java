package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
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

        put(
                "/additem",
                (request, response) -> {
                    final String orderParam = request.queryParams("orderId");
                    final String itemParam = request.queryParams("itemId");
                    final String quantityParam = request.queryParams("quantity");
                    log.debug("/order/additem/:itemid<{}>", itemParam);
                    final ObjectId orderId = new ObjectId(orderParam);
                    final ObjectId itemId = new ObjectId(itemParam);
                    // TODO: Make sure frontend enforces that quantity is a valid int! Or enforce
                    // here??
                    /* UPDATE (CSM):
                    enforce it on the frontend for sure!
                    */
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
                "/neworder",
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
                    response.redirect(String.format("/order/{}", order.getId().toHexString(), 301));
                    return order;
                });

        put(
                "/submitorder",
                (request, response) -> {
                    final String orderParam = request.queryParams("orderId");
                    log.debug("/submitorder/:orderid<{}>", orderParam);
                    final ObjectId orderId = new ObjectId(orderParam);
                    // TODO: Do I need to validate the order obtained is good?
                    /* UPDATE (CSM):
                    We could force validation via our front end, ie are all
                    the fields filled out? no? okay customer doesnt move forward
                    until mandatory fields are not null
                    */
                    Order order = orderController.getOrder(orderId);
                    order.setOrderStatus(OrderStatus.CONFIRMED);
                    // TODO: Create new delivery object and pass off to delivery controller
                    orderController.updateOrder(order);
                    return order;
                },
                jsonTransformer);

        delete(
                "/deleteorder",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Order order = mapper.readValue(request.body(), Order.class);
                    orderController.deleteOrder(order.getId());
                    response.type("application/json");
                    return order;
                });
    }
}
