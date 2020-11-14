package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DeliveryDriverController;
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

    @Override
    public void register() {
        log.info("DeliveryDriverView > register");

        get(
                "/deliveryDriver",
                (request, response) -> {
                    log.debug("/deliveryDriver");
                    response.type("application/json");
                    return deliveryDriverController.getDeliveryDrivers();
                },
                jsonTransformer);

        get(
                "/deliveryDriver/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("deliveryDriver/:id<{}>", paramId);
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
                "/deliveryDriver",
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

                    response.redirect(
                            String.format(
                                    "/deliveryDriver/{}", deliveryDriver.getId().toHexString()),
                            301);
                    return deliveryDriver;
                });

        put(
                "/deliveryDriver",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    DeliveryDriver deliveryDriver =
                            mapper.readValue(request.body(), DeliveryDriver.class);
                    if (!deliveryDriver.isValid()) {
                        response.status(400);
                        return "";
                    }

                    deliveryDriverController.updateDeliveryDriver(deliveryDriver);
                    return deliveryDriver;
                });

        delete(
                "/deliveryDriver",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    DeliveryDriver deliveryDriver =
                            mapper.readValue(request.body(), DeliveryDriver.class);

                    deliveryDriverController.deleteDeliveryDriver(deliveryDriver.getId());
                    return deliveryDriver;
                });
    }
}
