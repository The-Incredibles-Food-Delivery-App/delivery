package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Review;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalDateTime;
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
        defaultReview1.setReviewContent("Review content for this default review 1");
        defaultReview1.setTimeStamp(LocalDateTime.now());

        final Review defaultReview2 = new Review();
        defaultReview2.setRating(3.0);
        defaultReview2.setUsername("Danielle");
        // TODO: add in a restaurant id per recommendation, but how?
        // defaultReview2.setRestaurantID(2);
        defaultReview2.setReviewContent("Review content for this default review 2");
        defaultReview2.setTimeStamp(LocalDateTime.now());

        try {
            addReview(defaultReview1);
            addReview(defaultReview2);
        } catch (Exception e) {
            log.error("ReviewController > construct > adding default reviews > failure?");
            e.printStackTrace();
        }
    }

    /**
     * Returns the review corresponding to the given id
     *
     * @param uuid - the id of the review
     * @return the review corresponding to the given id
     */
    @Nullable
    public Review getReview(@Nonnull ObjectId uuid) {
        log.debug("ReviewController > getReview({})", uuid);
        return reviews.get(uuid);
    }

    /**
     * Returns all reviews in the review repository
     *
     * @return all reviews
     */
    @Nonnull
    public Collection<Review> getReviews() {
        log.debug("ReviewController > getReviews()");
        return reviews.getAll();
    }

    /**
     * Validates the ReviewRating is in the range of valid numbers.
     *
     * @return true if this ReviewRating is valid.
     */
    public boolean ratingIsValid(@Nonnull Review review) {
        if (review.getRating() < 0.0 || review.getRating() > 5.0) {
            return false;
        }
        return true;
    }

    /**
     * Validates the timestamp of the review is before or equal to the present time. TODO: (maybe)
     * Add check to see if customer has already submitted order
     *
     * @return true if this timestamp is valid.
     */
    public boolean timestampIsValid(@Nonnull Review review) {
        if (review.getTimeStamp().isAfter(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    /**
     * Adds a review to the repository
     *
     * @param review - the review to add
     * @throws InvalidReviewException - when the review given is invalid
     * @throws DuplicateKeyException - when the item id is already contained in the collection of
     *     review
     */
    @Nonnull
    public Review addReview(@Nonnull Review review)
            throws InvalidReviewException, DuplicateKeyException {
        log.debug("ReviewController > addReview(...)");
        if (!review.isValid() || !timestampIsValid(review) || !ratingIsValid(review)) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new InvalidReviewException("Review is invalid");
        }

        ObjectId id = review.getId();

        if (id != null && reviews.get(id) != null) {
            throw new DuplicateKeyException("Review already exists");
        }

        return reviews.add(review);
    }

    /**
     * Updates the given review
     *
     * @param item - the review object to update
     * @throws Exception
     */
    public void updateReview(@Nonnull Review review) throws Exception {
        log.debug("ReviewController > updateReview(...)");
        reviews.update(review);
    }

    /**
     * Deletes the review corresponding to the given id
     *
     * @param id - the id corresponding to the review to delete
     * @throws Exception
     */
    public void deleteReview(@Nonnull ObjectId id) throws Exception {
        log.debug("ReviewController > deleteReview(...)");
        reviews.delete(id);
    }
}
