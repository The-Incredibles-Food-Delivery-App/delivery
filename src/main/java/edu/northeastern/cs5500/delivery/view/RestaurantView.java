package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.RestaurantController;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RestaurantView implements View {

    @Inject
    RestaurantView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject RestaurantController restaurantController;

    @Override
    public void register() {
        log.info("RestaurantView > register");

        get(
                "/restaurant",
                (request, response) -> {
                    log.debug("/restaurant");
                    response.type("application/json");
                    return restaurantController.getRestaurants();
                },
                jsonTransformer);

        get(
                "/restaurant/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/restaurant/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Restaurant restaurant = restaurantController.getRestaurant(id);
                    if (restaurant == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return restaurant;
                },
                jsonTransformer);

        get(
                "/restaurants/:searchString",
                (request, response) -> {
                    final String searchString = request.params(":searchString");
                    log.debug("/restaurant/:searchString<{}>", searchString);
                    Collection<Restaurant> restaurants =
                            restaurantController.getRestaurantsByName(searchString);
                    response.type("application/json");
                    return restaurants;
                },
                jsonTransformer);

        post(
                "/restaurant",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Restaurant restaurant = mapper.readValue(request.body(), Restaurant.class);
                    if (!restaurant.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    restaurant.setId(null);
                    restaurant = restaurantController.addRestaurant(restaurant);

                    response.redirect(
                            String.format("/restaurant/{}", restaurant.getId().toHexString()), 301);
                    return restaurant;
                });

        put(
                "/restaurant",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Restaurant restaurant = mapper.readValue(request.body(), Restaurant.class);
                    if (!restaurant.isValid()) {
                        response.status(400);
                        return "";
                    }

                    restaurantController.updateRestaurant(restaurant);
                    return restaurant;
                });

        delete(
                "/restaurant",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Restaurant restaurant = mapper.readValue(request.body(), Restaurant.class);

                    restaurantController.deleteRestaurant(restaurant.getId());
                    return restaurant;
                });
    }
}
