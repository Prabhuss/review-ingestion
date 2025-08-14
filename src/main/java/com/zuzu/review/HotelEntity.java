package com.zuzu.review;

import jakarta.persistence.*;

@Entity
@Table(name = "hotels")
public class HotelEntity {
  @Id
  @Column(name = "hotel_id")
  private Long hotelId;

  @Column(name = "hotel_name", nullable = false)
  private String hotelName;

  public Long getHotelId() { return hotelId; }
  public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
  public String getHotelName() { return hotelName; }
  public void setHotelName(String hotelName) { this.hotelName = hotelName; }
}
