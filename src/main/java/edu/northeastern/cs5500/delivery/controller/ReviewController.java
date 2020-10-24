package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.Review;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class ReviewController {
    private final GenericRepository<Review> reviews;

    @Inject
    ReviewController(GenericRepository<Review> reviewRepository) {
        reviews = reviewRepository;

        log.info("ReviewController > construct");

        if (reviews.count() > 0) {
            return;
        }

        log.info("ReviewController > construct > adding default reviews");

        final Review defaultReview1 = new Review();
        defaultReview1.setRating(4.0);
        defaultReview1.setUsername("Mustafa");
        // TODO: add in a restaurant id per recommendation, but how?
        // defaultReview1.setRestaurantID(1);

        final Review defaultRecommendation2 = new Review();
        defaultReview1.setRating(3.0);
        defaultReview2.setUsername("Danielle");
        // TODO: add in a restaurant id per recommendation, but how?
        // defaultReview2.setRestaurantID(2);

        try {
            addReview(defaultReview1);
            addReview(defaultReview2);
        } catch (Exception e) {
            log.error("ReviewController > construct > adding default reviews > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Review getReview(@Nonnull ObjectId uuid) {
        log.debug("ReviewController > getReview({})", uuid);
        return reviews.get(uuid);
    }

    @Nonnull
    public Collection<Review> getReviews() {
        log.debug("ReviewController > getReviews()");
        return reviews.getAll();
    }

    @Nonnull
    public Review addReview(@Nonnull Review review) throws Exception {
        log.debug("ReviewController > addReview(...)");
        if (!review.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidReviewException");
        }

        ObjectId id = review.getId();

        if (id != null && review.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return review.add(review);
    }

    public void updateReview(@Nonnull Review review) throws Exception {
        log.debug("ReviewController > updateReview(...)");
        review.update(review);
    }

    public void deleteReview(@Nonnull ObjectId id) throws Exception {
        log.debug("ReviewController > deleteReview(...)");
        reviews.delete(id);
    }
}
