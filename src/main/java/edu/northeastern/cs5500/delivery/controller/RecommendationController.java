package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.Recommendation;
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
public class RecommendationController {
    private final GenericRepository<Recommendation> recommendations;

    @Inject
    RecommendationController(GenericRepository<Recommendation> recommendationRepository) {
        recommendations = recommendationRepository;

        log.info("RecommendationController > construct");

        if (recommendations.count() > 0) {
            return;
        }

        log.info("RecommendationController > construct > adding default recommendations");

        final Recommendation defaultRecommendation1 = new Recommendation();
        defaultRecommendation1.setUsername("Mustafa");
        // TODO: add in a restaurant id per recommendation, but how?
        // defaultRecommendation1.setRestaurantID(1);

        final Recommendation defaultRecommendation2 = new Recommendation();
        defaultRecommendation2.setUsername("Danielle");
        // TODO: add in a restaurant id per recommendation, but how?
        // defaultRecommendation2.setRestaurantID(2);

        try {
            addRecommendation(defaultRecommendation1);
            addRecommendation(defaultRecommendation2);
        } catch (Exception e) {
            log.error("RecommendationController > construct > adding default recommendations > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Recommendation getRecommendation(@Nonnull ObjectId uuid) {
        log.debug("RecommendationController > getRecommendation({})", uuid);
        return recommendations.get(uuid);
    }

    @Nonnull
    public Collection<Recommendation> getRecommendations() {
        log.debug("RecommendationController > getRecommendations()");
        return recommendations.getAll();
    }

    @Nonnull
    public Recommendation addRecommendation(@Nonnull Recommendation recommendation) throws Exception {
        log.debug("RecommendationController > addRecommendation(...)");
        if (!recommendation.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidRecommendationException");
        }

        ObjectId id = recommendation.getId();

        if (id != null && recommendations.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return recommendations.add(recommendation);
    }

    public void updateRecommendation(@Nonnull Recommendation recommendation) throws Exception {
        log.debug("RecommendationController > updateRecommendation(...)");
        recommendations.update(recommendation);
    }

    public void deleteRecommendation(@Nonnull ObjectId id) throws Exception {
        log.debug("RecommendationController > deleteRecommendation(...)");
        recommendations.delete(id);
    }
}
