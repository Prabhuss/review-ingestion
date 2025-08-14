package com.zuzu.review;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for ReviewEntity to ensure reviews from different providers
 * with the same review ID are stored as distinct entries
 */
public class ReviewId implements Serializable {
  private Long hotelReviewId;
  private Integer providerId;

  public ReviewId() {}

  public ReviewId(Long hotelReviewId, Integer providerId) {
    this.hotelReviewId = hotelReviewId;
    this.providerId = providerId;
  }

  public Long getHotelReviewId() { return hotelReviewId; }
  public void setHotelReviewId(Long hotelReviewId) { this.hotelReviewId = hotelReviewId; }
  public Integer getProviderId() { return providerId; }
  public void setProviderId(Integer providerId) { this.providerId = providerId; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReviewId reviewId = (ReviewId) o;
    return Objects.equals(hotelReviewId, reviewId.hotelReviewId) &&
           Objects.equals(providerId, reviewId.providerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hotelReviewId, providerId);
  }
}