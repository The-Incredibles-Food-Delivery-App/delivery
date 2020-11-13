package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.RecommendationController;
import edu.northeastern.cs5500.delivery.model.Recommendation;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RecommendationView implements View {

    @Inject
    RecommendationView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject RecommendationController recommendationController;

    @Override
    public void register() {
        log.info("RecommendationyView > register");

        get(
                "/recommendation",
                (request, response) -> {
                    log.debug("/recommendation");
                    response.type("application/json");
                    return recommendationController.getRecommendations();
                },
                jsonTransformer);

        get(
                "/recommendation/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/recommendation/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Recommendation recommendation = recommendationController.getRecommendation(id);
                    if (recommendation == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return recommendation;
                },
                jsonTransformer);

        post(
                "/recommendation",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Recommendation recommendation = mapper.readValue(request.body(), Recommendation.class);
                    if (!recommendation.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    recommendation.setId(null);
                    recommendation = recommendationController.addRecommendation(recommendation);

                    response.redirect(
                            String.format("/recommendation/{}", recommendation.getId().toHexString()), 301);
                    return recommendation;
                });

        put(
                "/recommendation",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Recommendation recommendation = mapper.readValue(request.body(), Recommendation.class);
                    if (!recommendation.isValid()) {
                        response.status(400);
                        return "";
                    }

                    recommendationController.updateRecommendation(recommendation);
                    return recommendation;
                });

        delete(
                "/recommendation",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Recommendation recommendation = mapper.readValue(request.body(), Recommendation.class);

                    recommendationController.deleteRecommendation(recommendation.getId());
                    return recommendation;
                });
    }
}
