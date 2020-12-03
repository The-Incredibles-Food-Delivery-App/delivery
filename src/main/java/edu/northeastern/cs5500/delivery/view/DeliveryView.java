package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DeliveryController;
import edu.northeastern.cs5500.delivery.controller.DeliveryManager;
import edu.northeastern.cs5500.delivery.model.Delivery;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DeliveryView implements View {

    @Inject
    DeliveryView() {}

    @Inject JsonTransformer jsonTransformer;
    @Inject DeliveryManager deliveryManager;
    @Inject DeliveryController deliveryController;

    @Override
    public void register() {
        log.info("DeliveryView > register");

        get(
                "/delivery",
                (request, response) -> {
                    log.debug("/delivery");
                    response.type("application/json");
                    return deliveryController.getDeliveries();
                },
                jsonTransformer);

        get(
                "/delivery/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/delivery/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Delivery delivery = deliveryController.getDelivery(id);
                    if (delivery == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return delivery;
                },
                jsonTransformer);

        post(
                "/delivery",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Delivery delivery = mapper.readValue(request.body(), Delivery.class);
                    if (!delivery.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    delivery.setId(null);
                    delivery = deliveryController.addDelivery(delivery);
                    response.redirect(
                            String.format("/delivery/{}", delivery.getId().toHexString()), 301);
                    return delivery;
                });

        put(
                "/deliver",
                (request, response) -> {
                    final String deliveryParam = request.queryParams("deliveryId");
                    log.debug("/deliver/:deliveryid<{}>", deliveryParam);
                    final ObjectId deliveryId = new ObjectId(deliveryParam);
                    Delivery delivery = deliveryController.getDelivery(deliveryId);
                    if (delivery == null || !delivery.isValid()) {
                        response.status(400);
                        return "";
                    }
                    deliveryManager.sendForDelivery(delivery);
                    return delivery;
                },
                jsonTransformer);

        put(
                "/delivery",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Delivery delivery = mapper.readValue(request.body(), Delivery.class);
                    if (!delivery.isValid()) {
                        response.status(400);
                        return "";
                    }

                    deliveryController.updateDelivery(delivery);
                    return delivery;
                });

        put(
                "/completeDelivery",
                (request, response) -> {
                    final String deliveryParam = request.queryParams("deliveryId");
                    log.debug("/completeDelivery/:deliveryid<{}>", deliveryParam);
                    final ObjectId deliveryId = new ObjectId(deliveryParam);
                    Delivery delivery = deliveryController.getDelivery(deliveryId);
                    if (delivery == null || !delivery.isValid()) {
                        response.status(400);
                        return "";
                    }
                    Delivery completedDelivery = deliveryController.completeDelivery(delivery);
                    return completedDelivery;
                },
                jsonTransformer);

        delete(
                "/delivery",
                (request, response) -> {
                    final String deliveryParam = request.queryParams("deliveryId");
                    final ObjectId deliveryId = new ObjectId(deliveryParam);
                    deliveryController.deleteDelivery(deliveryId);
                    response.type("application/json");
                    log.debug("Successfully Deleted Delivery id <{}>", deliveryId);
                    // TODO: Is this appropriate?
                    return null;
                });
    }
}
