package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.ReviewController;
import edu.northeastern.cs5500.delivery.model.Review;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class ReviewView implements View {

    @Inject
    ReviewView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject ReviewController reviewController;

    @Override
    public void register() {
        log.info("ReviewView > register");

        get(
                "/review",
                (request, response) -> {
                    log.debug("/review");
                    response.type("application/json");
                    return reviewController.getReviews();
                },
                jsonTransformer);

        get(
                "/review/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/review/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Review review = reviewController.getReview(id);
                    if (review == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return review;
                },
                jsonTransformer);

        post(
                "/review",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Review review = mapper.readValue(request.body(), Review.class);
                    if (!review.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    review.setId(null);
                    review = reviewController.addReview(review);

                    response.redirect(
                            String.format("/review/{}", review.getId().toHexString()), 301);
                    return review;
                });

        put(
                "/review",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Review review = mapper.readValue(request.body(), Review.class);
                    if (!review.isValid()) {
                        response.status(400);
                        return "";
                    }

                    reviewController.updateReview(review);
                    return review;
                });

        delete(
                "/review",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Review review = mapper.readValue(request.body(), Review.class);

                    reviewController.deleteReview(review.getId());
                    return review;
                });
    }
}
