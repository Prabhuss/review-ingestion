package com.zuzu.review;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles JSON parsing and conversion to ReviewData domain model.
 * Includes proper error handling and logging for parsing issues.
 */
public class ReviewDataParser {
    private static final Logger logger = LoggerFactory.getLogger(ReviewDataParser.class);
    private final ObjectMapper objectMapper;

    public ReviewDataParser() {
        this.objectMapper = new ObjectMapper();
    }

    public ReviewData parseJson(String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);
        ReviewData data = new ReviewData();

        // Parse basic hotel and platform info
        data.setHotelId(getLong(root, "hotelId"));
        data.setHotelName(getText(root, "hotelName"));
        data.setPlatform(getText(root, "platform"));

        // Parse comment section
        JsonNode comment = root.path("comment");
        if (!comment.isMissingNode()) {
            parseCommentSection(comment, data);
        }

        // Parse reviewer section from comment.reviewerInfo
        JsonNode reviewerInfo = comment.path("reviewerInfo");
        if (!reviewerInfo.isMissingNode()) {
            parseReviewerSection(reviewerInfo, data);
        }

        // Parse overallByProviders section
        JsonNode overallByProviders = root.path("overallByProviders");
        if (!overallByProviders.isMissingNode() && overallByProviders.isArray() && overallByProviders.size() > 0) {
            // Use the first provider's data (assuming single provider per review)
            JsonNode firstProvider = overallByProviders.get(0);
            parseOverallSection(firstProvider, data);
            
            // Parse grades from the first provider
            JsonNode grades = firstProvider.path("grades");
            if (!grades.isMissingNode()) {
                parseGradesSection(grades, data);
            }
        }

        return data;
    }

    private void parseCommentSection(JsonNode comment, ReviewData data) {
        data.setHotelReviewId(getLong(comment, "hotelReviewId"));
        data.setProviderId(getInt(comment, "providerId"));
        data.setRating(getBigDecimal(comment, "rating"));
        data.setReviewComments(getText(comment, "reviewComments"));
        data.setReviewDate(parseOffsetDateTime(getText(comment, "reviewDate")));
        data.setTranslateSource(getText(comment, "translateSource"));
        data.setResponseFromHotel(getText(comment, "responseFromHotel"));
        data.setResponseDate(parseOffsetDateTime(getText(comment, "responseDate")));
    }

    private void parseReviewerSection(JsonNode reviewer, ReviewData data) {
        data.setDisplayMemberName(getText(reviewer, "displayMemberName"));
        data.setCountryName(getText(reviewer, "countryName"));
        data.setReviewGroupName(getText(reviewer, "reviewGroupName"));
        data.setMemberContributions(getInt(reviewer, "memberContributions"));
        data.setHelpfulVotes(getInt(reviewer, "helpfulVotes"));
        data.setAverageScore(getBigDecimal(reviewer, "averageScore"));
        data.setTotalReviewsCount(getInt(reviewer, "totalReviewsCount"));
        data.setReviewerLevel(getInt(reviewer, "reviewerLevel"));
        data.setIsVerified(getBoolean(reviewer, "isVerified"));
        data.setDateOfStay(getText(reviewer, "dateOfStay"));
        data.setRoomType(getText(reviewer, "roomType"));
        data.setTravelType(getText(reviewer, "travelType"));
    }

    private void parseOverallSection(JsonNode overall, ReviewData data) {
        data.setOverallScore(getBigDecimal(overall, "overallScore"));
        data.setOverallReviewsCount(getInt(overall, "reviewCount"));
    }

    private void parseGradesSection(JsonNode grades, ReviewData data) {
        data.setCleanlinessScore(getBigDecimal(grades, "Cleanliness"));
        data.setComfortScore(getBigDecimal(grades, "Room comfort and quality"));
        data.setLocationScore(getBigDecimal(grades, "Location"));
        data.setServiceScore(getBigDecimal(grades, "Service"));
        data.setFacilitiesScore(getBigDecimal(grades, "Facilities"));
        data.setValueForMoneyScore(getBigDecimal(grades, "Value for money"));
    }

    private Long getLong(JsonNode node, String fieldName) {
        JsonNode field = node.path(fieldName);
        if (field.isMissingNode() || field.isNull()) {
            return null;
        }
        try {
            return field.asLong();
        } catch (Exception e) {
            logger.warn("Failed to parse long field '{}' with value '{}': {}", fieldName, field.asText(), e.getMessage());
            return null;
        }
    }

    private Integer getInt(JsonNode node, String fieldName) {
        JsonNode field = node.path(fieldName);
        if (field.isMissingNode() || field.isNull()) {
            return null;
        }
        try {
            return field.asInt();
        } catch (Exception e) {
            logger.warn("Failed to parse int field '{}' with value '{}': {}", fieldName, field.asText(), e.getMessage());
            return null;
        }
    }

    private String getText(JsonNode node, String fieldName) {
        JsonNode field = node.path(fieldName);
        if (field.isMissingNode() || field.isNull()) {
            return null;
        }
        String text = field.asText();
        return text.isEmpty() ? null : text;
    }

    private Boolean getBoolean(JsonNode node, String fieldName) {
        JsonNode field = node.path(fieldName);
        if (field.isMissingNode() || field.isNull()) {
            return null;
        }
        try {
            return field.asBoolean();
        } catch (Exception e) {
            logger.warn("Failed to parse boolean field '{}' with value '{}': {}", fieldName, field.asText(), e.getMessage());
            return null;
        }
    }

    private BigDecimal getBigDecimal(JsonNode node, String fieldName) {
        JsonNode field = node.path(fieldName);
        if (field.isMissingNode() || field.isNull()) {
            return null;
        }
        try {
            return field.decimalValue();
        } catch (Exception e) {
            logger.warn("Failed to parse decimal field '{}' with value '{}': {}", fieldName, field.asText(), e.getMessage());
            return null;
        }
    }

    private OffsetDateTime parseOffsetDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return OffsetDateTime.parse(dateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                return OffsetDateTime.parse(dateStr + "T00:00:00Z");
            } catch (DateTimeParseException e2) {
                logger.warn("Failed to parse date '{}': {}", dateStr, e2.getMessage());
                return null;
            }
        }
    }
}