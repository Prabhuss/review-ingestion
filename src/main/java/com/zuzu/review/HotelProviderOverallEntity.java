package com.zuzu.review;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hotel_provider_overall")
public class HotelProviderOverallEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "hotel_id", nullable = false)
  private Long hotelId;

  @Column(name = "provider_id", nullable = false)
  private Integer providerId;

  @Column(name = "overall_score", precision = 3, scale = 1)
  private BigDecimal overallScore;

  @Column(name = "review_count")
  private Integer reviewCount;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getHotelId() { return hotelId; }
  public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
  public Integer getProviderId() { return providerId; }
  public void setProviderId(Integer providerId) { this.providerId = providerId; }
  public BigDecimal getOverallScore() { return overallScore; }
  public void setOverallScore(BigDecimal overallScore) { this.overallScore = overallScore; }
  public Integer getReviewCount() { return reviewCount; }
  public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
}
