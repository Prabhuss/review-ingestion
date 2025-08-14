package com.zuzu.review;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Domain model representing review data extracted from JSON.
 * This separates the parsing logic from the persistence logic.
 */
public class ReviewData {
    private Long hotelId;
    private String hotelName;
    private String platform;
    private Integer providerId;
    private String providerName;
    
    // Review details
    private Long hotelReviewId;
    private BigDecimal rating;
    private String reviewComments;
    private OffsetDateTime reviewDate;
    private String translateSource;
    private String responseFromHotel;
    private OffsetDateTime responseDate;
    
    // Reviewer details
    private String displayMemberName;
    private String countryName;
    private String reviewGroupName;
    private Integer memberContributions;
    private Integer helpfulVotes;
    private BigDecimal averageScore;
    private Integer totalReviewsCount;
    private Integer reviewerLevel;
    private Boolean isVerified;
    private String dateOfStay;
    private String roomType;
    private String travelType;
    
    // Overall aggregates
    private BigDecimal overallScore;
    private Integer overallReviewsCount;
    
    // Grade aggregates
    private BigDecimal cleanlinessScore;
    private BigDecimal comfortScore;
    private BigDecimal locationScore;
    private BigDecimal serviceScore;
    private BigDecimal facilitiesScore;
    private BigDecimal valueForMoneyScore;

    // Constructors
    public ReviewData() {}

    // Getters and setters
    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    
    public Integer getProviderId() { return providerId; }
    public void setProviderId(Integer providerId) { this.providerId = providerId; }
    
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    
    public Long getHotelReviewId() { return hotelReviewId; }
    public void setHotelReviewId(Long hotelReviewId) { this.hotelReviewId = hotelReviewId; }
    
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    
    public String getReviewComments() { return reviewComments; }
    public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }
    
    public OffsetDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(OffsetDateTime reviewDate) { this.reviewDate = reviewDate; }
    
    public String getTranslateSource() { return translateSource; }
    public void setTranslateSource(String translateSource) { this.translateSource = translateSource; }
    
    public String getResponseFromHotel() { return responseFromHotel; }
    public void setResponseFromHotel(String responseFromHotel) { this.responseFromHotel = responseFromHotel; }
    
    public OffsetDateTime getResponseDate() { return responseDate; }
    public void setResponseDate(OffsetDateTime responseDate) { this.responseDate = responseDate; }
    
    public String getDisplayMemberName() { return displayMemberName; }
    public void setDisplayMemberName(String displayMemberName) { this.displayMemberName = displayMemberName; }
    
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    
    public String getReviewGroupName() { return reviewGroupName; }
    public void setReviewGroupName(String reviewGroupName) { this.reviewGroupName = reviewGroupName; }
    
    public Integer getMemberContributions() { return memberContributions; }
    public void setMemberContributions(Integer memberContributions) { this.memberContributions = memberContributions; }
    
    public Integer getHelpfulVotes() { return helpfulVotes; }
    public void setHelpfulVotes(Integer helpfulVotes) { this.helpfulVotes = helpfulVotes; }
    
    public BigDecimal getAverageScore() { return averageScore; }
    public void setAverageScore(BigDecimal averageScore) { this.averageScore = averageScore; }
    
    public Integer getTotalReviewsCount() { return totalReviewsCount; }
    public void setTotalReviewsCount(Integer totalReviewsCount) { this.totalReviewsCount = totalReviewsCount; }
    
    public Integer getReviewerLevel() { return reviewerLevel; }
    public void setReviewerLevel(Integer reviewerLevel) { this.reviewerLevel = reviewerLevel; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public String getDateOfStay() { return dateOfStay; }
    public void setDateOfStay(String dateOfStay) { this.dateOfStay = dateOfStay; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public String getTravelType() { return travelType; }
    public void setTravelType(String travelType) { this.travelType = travelType; }
    
    public BigDecimal getOverallScore() { return overallScore; }
    public void setOverallScore(BigDecimal overallScore) { this.overallScore = overallScore; }
    
    public Integer getOverallReviewsCount() { return overallReviewsCount; }
    public void setOverallReviewsCount(Integer overallReviewsCount) { this.overallReviewsCount = overallReviewsCount; }
    
    public BigDecimal getCleanlinessScore() { return cleanlinessScore; }
    public void setCleanlinessScore(BigDecimal cleanlinessScore) { this.cleanlinessScore = cleanlinessScore; }
    
    public BigDecimal getComfortScore() { return comfortScore; }
    public void setComfortScore(BigDecimal comfortScore) { this.comfortScore = comfortScore; }
    
    public BigDecimal getLocationScore() { return locationScore; }
    public void setLocationScore(BigDecimal locationScore) { this.locationScore = locationScore; }
    
    public BigDecimal getServiceScore() { return serviceScore; }
    public void setServiceScore(BigDecimal serviceScore) { this.serviceScore = serviceScore; }
    
    public BigDecimal getFacilitiesScore() { return facilitiesScore; }
    public void setFacilitiesScore(BigDecimal facilitiesScore) { this.facilitiesScore = facilitiesScore; }
    
    public BigDecimal getValueForMoneyScore() { return valueForMoneyScore; }
    public void setValueForMoneyScore(BigDecimal valueForMoneyScore) { this.valueForMoneyScore = valueForMoneyScore; }
}