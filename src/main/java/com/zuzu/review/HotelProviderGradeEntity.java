package com.zuzu.review;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hotel_provider_grades")
public class HotelProviderGradeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "hotel_id", nullable = false)
  private Long hotelId;

  @Column(name = "provider_id", nullable = false)
  private Integer providerId;

  @Column(name = "category_name", nullable = false)
  private String categoryName;

  @Column(name = "score", precision = 3, scale = 1)
  private BigDecimal score;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getHotelId() { return hotelId; }
  public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
  public Integer getProviderId() { return providerId; }
  public void setProviderId(Integer providerId) { this.providerId = providerId; }
  public String getCategoryName() { return categoryName; }
  public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
  public BigDecimal getScore() { return score; }
  public void setScore(BigDecimal score) { this.score = score; }
}
