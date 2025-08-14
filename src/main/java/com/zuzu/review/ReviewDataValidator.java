package com.zuzu.review;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Handles validation of ReviewData and related entities.
 * Provides centralized validation logic with proper error reporting.
 */
public class ReviewDataValidator {
    private static final Logger logger = LoggerFactory.getLogger(ReviewDataValidator.class);
    private final Validator validator;

    public ReviewDataValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateReviewData(ReviewData data) {
        if (data.getHotelId() == null) {
            throw new IllegalArgumentException("Hotel ID is required");
        }
        
        if (data.getHotelReviewId() == null) {
            throw new IllegalArgumentException("Hotel Review ID is required");
        }
        
        if (data.getProviderId() == null) {
            throw new IllegalArgumentException("Provider ID is required");
        }

        logger.debug("ReviewData validation passed for hotel {} review {}", 
                    data.getHotelId(), data.getHotelReviewId());
    }

    public void validateReviewEntity(ReviewEntity entity) {
        Set<ConstraintViolation<ReviewEntity>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Review validation failed: ");
            for (ConstraintViolation<ReviewEntity> violation : violations) {
                sb.append(violation.getPropertyPath())
                  .append(" ")
                  .append(violation.getMessage())
                  .append("; ");
            }
            String errorMessage = sb.toString();
            logger.error("Review validation failed: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        
        logger.debug("ReviewEntity validation passed for review {}-{}", 
                    entity.getHotelReviewId(), entity.getProviderId());
    }

    public void validateReviewerEntity(ReviewerEntity entity) {
        Set<ConstraintViolation<ReviewerEntity>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Reviewer validation failed: ");
            for (ConstraintViolation<ReviewerEntity> violation : violations) {
                sb.append(violation.getPropertyPath())
                  .append(" ")
                  .append(violation.getMessage())
                  .append("; ");
            }
            String errorMessage = sb.toString();
            logger.warn("Reviewer validation failed: {}", errorMessage);
            // Don't throw for reviewer validation - just log warning
        }
    }
}