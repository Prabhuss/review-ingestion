package com.zuzu.review;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles persistence operations for review data.
 * Separates database logic from parsing and validation.
 */
public class ReviewPersistenceService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewPersistenceService.class);
    private final ReviewRepository repository;
    private final ReviewDataValidator validator;

    public ReviewPersistenceService(ReviewRepository repository, ReviewDataValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Long persistReviewData(ReviewData data) throws Exception {
        return repository.processInTransaction(em -> {
            try {
                // Upsert hotel and provider
                upsertHotelAndProvider(em, data);
                
                // Handle reviewer
                Long reviewerId = handleReviewer(em, data);
                
                // Create and persist review
                ReviewEntity review = createReviewEntity(data, reviewerId);
                validator.validateReviewEntity(review);
                Long reviewId = repository.upsertReview(em, review);
                
                // Persist aggregates
                persistAggregates(em, data);
                
                logger.info("Successfully persisted review {}-{} for hotel {}", 
                           data.getHotelReviewId(), data.getProviderId(), data.getHotelId());
                
                return reviewId;
            } catch (Exception e) {
                logger.error("Failed to persist review data for hotel {} review {}: {}", 
                            data.getHotelId(), data.getHotelReviewId(), e.getMessage(), e);
                throw e;
            }
        });
    }

    private void upsertHotelAndProvider(EntityManager em, ReviewData data) {
        // Upsert hotel
        repository.upsertHotel(em, data.getHotelId(), data.getHotelName());
        
        // Determine provider info
        String providerName = data.getProviderName();
        if (providerName == null && data.getPlatform() != null) {
            providerName = data.getPlatform();
        }
        
        // Upsert provider
        repository.upsertProvider(em, data.getProviderId(), providerName);
    }

    private Long handleReviewer(EntityManager em, ReviewData data) {
        if (hasReviewerInfo(data)) {
            ReviewerEntity reviewer = createReviewerEntity(data);
            validator.validateReviewerEntity(reviewer);
            return repository.upsertReviewer(em, reviewer);
        }
        
        // Create a default/anonymous reviewer if no reviewer info is available
        // since reviewerId is required in ReviewEntity
        ReviewerEntity defaultReviewer = new ReviewerEntity();
        defaultReviewer.setDisplayMemberName("Anonymous");
        defaultReviewer.setCountryName("Unknown");
        defaultReviewer.setReviewGroupName("Guest");
        defaultReviewer.setReviewerReviewedCount(0);
        
        return repository.upsertReviewer(em, defaultReviewer);
    }

    private boolean hasReviewerInfo(ReviewData data) {
        return data.getDisplayMemberName() != null || 
               data.getCountryName() != null || 
               data.getReviewGroupName() != null;
    }

    private ReviewerEntity createReviewerEntity(ReviewData data) {
        ReviewerEntity reviewer = new ReviewerEntity();
        reviewer.setDisplayMemberName(data.getDisplayMemberName());
        reviewer.setCountryName(data.getCountryName());
        reviewer.setReviewGroupName(data.getReviewGroupName());
        // Map to existing fields in ReviewerEntity
        reviewer.setReviewerReviewedCount(data.getMemberContributions());
        return reviewer;
    }

    private ReviewEntity createReviewEntity(ReviewData data, Long reviewerId) {
        ReviewEntity review = new ReviewEntity();
        review.setHotelReviewId(data.getHotelReviewId());
        review.setHotelId(data.getHotelId());
        review.setProviderId(data.getProviderId());
        review.setReviewerId(reviewerId);
        review.setRating(data.getRating());
        review.setReviewComments(data.getReviewComments());
        review.setReviewDate(data.getReviewDate());
        review.setTranslateSource(data.getTranslateSource());
        // Map response fields to existing entity fields
        review.setResponseText(data.getResponseFromHotel());
        review.setFormattedResponseDate(data.getResponseDate() != null ? data.getResponseDate().toString() : null);
        // Note: dateOfStay, roomType, travelType don't exist as direct fields in ReviewEntity
        return review;
    }

    private void persistAggregates(EntityManager em, ReviewData data) {
        // Persist overall scores if present
        if (data.getOverallScore() != null || data.getOverallReviewsCount() != null) {
            HotelProviderOverallEntity overall = new HotelProviderOverallEntity();
            overall.setHotelId(data.getHotelId());
            overall.setProviderId(data.getProviderId());
            overall.setOverallScore(data.getOverallScore());
            overall.setReviewCount(data.getOverallReviewsCount()); // Use setReviewCount instead
            repository.upsertOverall(em, overall);
        }

        // Persist grade scores if present
        persistGradeIfPresent(em, data, "cleanliness", data.getCleanlinessScore());
        persistGradeIfPresent(em, data, "comfort", data.getComfortScore());
        persistGradeIfPresent(em, data, "location", data.getLocationScore());
        persistGradeIfPresent(em, data, "service", data.getServiceScore());
        persistGradeIfPresent(em, data, "facilities", data.getFacilitiesScore());
        persistGradeIfPresent(em, data, "valueForMoney", data.getValueForMoneyScore());
    }

    private void persistGradeIfPresent(EntityManager em, ReviewData data, String category, 
                                     java.math.BigDecimal score) {
        if (score != null) {
            HotelProviderGradeEntity grade = new HotelProviderGradeEntity();
            grade.setHotelId(data.getHotelId());
            grade.setProviderId(data.getProviderId());
            grade.setCategoryName(category); // Use setCategoryName instead of setCategory
            grade.setScore(score);
            repository.upsertGrade(em, grade);
        }
    }
}