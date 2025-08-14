package com.zuzu.review;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Orchestrates the review processing workflow.
 * Delegates parsing, validation, and persistence to specialized services.
 */
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    
    private final ReviewDataParser parser;
    private final ReviewDataValidator validator;
    private final ReviewPersistenceService persistenceService;

    public ReviewService(ReviewRepository repo) {
        this.parser = new ReviewDataParser();
        
        ValidatorFactory factory = jakarta.validation.Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator())
                .buildValidatorFactory();
        Validator jakartaValidator = factory.getValidator();
        this.validator = new ReviewDataValidator(jakartaValidator);
        
        this.persistenceService = new ReviewPersistenceService(repo, validator);
    }

    /**
     * Parse JSON and persist review data using modular approach
     */
    public Long processJson(String json) throws Exception {
        try {
            logger.debug("Processing JSON review data");
            
            // Parse JSON to domain model
            ReviewData reviewData = parser.parseJson(json);
            
            // Validate the parsed data
            validator.validateReviewData(reviewData);
            
            // Persist to database
            Long reviewId = persistenceService.persistReviewData(reviewData);
            
            logger.info("Successfully processed review {}-{}", 
                       reviewData.getHotelReviewId(), reviewData.getProviderId());
            
            return reviewId;
        } catch (Exception e) {
            logger.error("Failed to process JSON review data: {}", e.getMessage(), e);
            throw e;
        }
    }
}