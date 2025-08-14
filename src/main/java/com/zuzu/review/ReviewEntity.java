package com.zuzu.review;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "reviews")
@IdClass(ReviewId.class)
public class ReviewEntity {
  @Id
  @Column(name = "hotel_review_id")
  private Long hotelReviewId;

  @Id
  @Column(name = "provider_id", nullable = false)
  private Integer providerId;

  @Column(name = "hotel_id", nullable = false)
  private Long hotelId;

  @Column(name = "reviewer_id", nullable = false)
  private Long reviewerId;

  @Column(precision = 3, scale = 1)
  private BigDecimal rating;

  @Column(name = "rating_text")
  private String ratingText;

  @Column(name = "check_in_month_year")
  private String checkInMonthYear;

  @Column(name = "review_date")
  private OffsetDateTime reviewDate;

  @Column(name = "review_title")
  private String reviewTitle;

  @Column(name = "review_comments", columnDefinition = "text")
  private String reviewComments;

  @Column(name = "review_negatives", columnDefinition = "text")
  private String reviewNegatives;

  @Column(name = "review_positives", columnDefinition = "text")
  private String reviewPositives;

  @Column(name = "encrypted_review_data")
  private String encryptedReviewData;

  @Column(name = "responder_name")
  private String responderName;

  @Column(name = "response_date_text")
  private String responseDateText;

  @Column(name = "response_translate_source")
  private String responseTranslateSource;

  @Column(name = "response_text")
  private String responseText;

  @Column(name = "translate_source")
  private String translateSource;

  @Column(name = "translate_target")
  private String translateTarget;

  @Column(name = "is_show_review_response")
  private Boolean isShowReviewResponse;

  @Column(name = "original_title")
  private String originalTitle;

  @Column(name = "original_comment", columnDefinition = "text")
  private String originalComment;

  @Column(name = "formatted_response_date")
  private String formattedResponseDate;

  @Column(name = "review_provider_text")
  private String reviewProviderText;

  public Long getHotelReviewId() { return hotelReviewId; }
  public void setHotelReviewId(Long hotelReviewId) { this.hotelReviewId = hotelReviewId; }
  public Long getHotelId() { return hotelId; }
  public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
  public Integer getProviderId() { return providerId; }
  public void setProviderId(Integer providerId) { this.providerId = providerId; }
  public Long getReviewerId() { return reviewerId; }
  public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
  public BigDecimal getRating() { return rating; }
  public void setRating(BigDecimal rating) { this.rating = rating; }
  public String getRatingText() { return ratingText; }
  public void setRatingText(String ratingText) { this.ratingText = ratingText; }
  public String getCheckInMonthYear() { return checkInMonthYear; }
  public void setCheckInMonthYear(String checkInMonthYear) { this.checkInMonthYear = checkInMonthYear; }
  public OffsetDateTime getReviewDate() { return reviewDate; }
  public void setReviewDate(OffsetDateTime reviewDate) { this.reviewDate = reviewDate; }
  public String getReviewTitle() { return reviewTitle; }
  public void setReviewTitle(String reviewTitle) { this.reviewTitle = reviewTitle; }
  public String getReviewComments() { return reviewComments; }
  public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }
  public String getReviewNegatives() { return reviewNegatives; }
  public void setReviewNegatives(String reviewNegatives) { this.reviewNegatives = reviewNegatives; }
  public String getReviewPositives() { return reviewPositives; }
  public void setReviewPositives(String reviewPositives) { this.reviewPositives = reviewPositives; }
  public String getEncryptedReviewData() { return encryptedReviewData; }
  public void setEncryptedReviewData(String encryptedReviewData) { this.encryptedReviewData = encryptedReviewData; }
  public String getResponderName() { return responderName; }
  public void setResponderName(String responderName) { this.responderName = responderName; }
  public String getResponseDateText() { return responseDateText; }
  public void setResponseDateText(String responseDateText) { this.responseDateText = responseDateText; }
  public String getResponseTranslateSource() { return responseTranslateSource; }
  public void setResponseTranslateSource(String responseTranslateSource) { this.responseTranslateSource = responseTranslateSource; }
  public String getResponseText() { return responseText; }
  public void setResponseText(String responseText) { this.responseText = responseText; }
  public String getTranslateSource() { return translateSource; }
  public void setTranslateSource(String translateSource) { this.translateSource = translateSource; }
  public String getTranslateTarget() { return translateTarget; }
  public void setTranslateTarget(String translateTarget) { this.translateTarget = translateTarget; }
  public Boolean getShowReviewResponse() { return isShowReviewResponse; }
  public void setShowReviewResponse(Boolean showReviewResponse) { isShowReviewResponse = showReviewResponse; }
  public String getOriginalTitle() { return originalTitle; }
  public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }
  public String getOriginalComment() { return originalComment; }
  public void setOriginalComment(String originalComment) { this.originalComment = originalComment; }
  public String getFormattedResponseDate() { return formattedResponseDate; }
  public void setFormattedResponseDate(String formattedResponseDate) { this.formattedResponseDate = formattedResponseDate; }
  public String getReviewProviderText() { return reviewProviderText; }
  public void setReviewProviderText(String reviewProviderText) { this.reviewProviderText = reviewProviderText; }
}
