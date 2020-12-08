package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DeliveryDriverController;
import edu.northeastern.cs5500.delivery.controller.DeliveryDriverManager;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DeliveryDriverView implements View {

    @Inject
    DeliveryDriverView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject DeliveryDriverController deliveryDriverController;
    @Inject DeliveryDriverManager deliveryDriverManager;

    @Override
    public void register() {
        log.info("DeliveryDriverView > register");

        get(
                "/deliverydriver",
                (request, response) -> {
                    log.debug("/deliverydriver");
                    response.type("application/json");
                    return deliveryDriverController.getDeliveryDrivers();
                },
                jsonTransformer);

        get(
                "/deliverydriver/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/deliverydriver/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    DeliveryDriver deliveryDriver = deliveryDriverController.getDeliveryDriver(id);
                    if (deliveryDriver == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return deliveryDriver;
                },
                jsonTransformer);

        post(
                "/deliverydriver",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    DeliveryDriver deliveryDriver =
                            mapper.readValue(request.body(), DeliveryDriver.class);
                    if (!deliveryDriver.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    deliveryDriver.setId(null);
                    deliveryDriver = deliveryDriverController.addDeliveryDriver(deliveryDriver);
                    deliveryDriver.setCurrentlyWorking(false);
                    // Fills the queue of the deliveryDriverManager queue with the newly added
                    // driver (currently working == false)
                    deliveryDriverManager.fillQueue(deliveryDriver);

                    response.redirect(
                            String.format(
                                    "/deliverydriver/{}", deliveryDriver.getId().toHexString()),
                            301);
                    return deliveryDriver;
                });

        put(
                "/deliverydriver",
                (request, response) -> {
                    final String deliveryDriverParam = request.queryParams("deliveryDriverId");
                    log.debug("/deliverydriver/:deliverydriverid<{}>", deliveryDriverParam);
                    final ObjectId deliveryDriverId = new ObjectId(deliveryDriverParam);
                    DeliveryDriver deliveryDriver =
                            deliveryDriverController.getDeliveryDriver(deliveryDriverId);
                    if (deliveryDriver == null || !deliveryDriver.isValid()) {
                        response.status(400);
                        return "";
                    }
                    deliveryDriverController.updateDeliveryDriver(deliveryDriver);
                    return deliveryDriver;
                },
                jsonTransformer);

        delete(
                "/deliverydriver",
                (request, response) -> {
                    final String deliveryDriverParam = request.queryParams("deliveryDriverId");
                    final ObjectId deliveryDriverId = new ObjectId(deliveryDriverParam);
                    deliveryDriverController.deleteDeliveryDriver(deliveryDriverId);

                    response.type("application/json");
                    log.debug("Successfully Deleted Delivery Driver id <{}>", deliveryDriverId);
                    // TODO: Is this appropriate?
                    return null;
                });
    }
}
