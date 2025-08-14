package com.zuzu.review;

import jakarta.persistence.*;

@Entity
@Table(name = "reviewers")
public class ReviewerEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reviewer_id")
  private Long reviewerId;

  @Column(name = "display_member_name")
  private String displayMemberName;

  @Column(name = "country_id")
  private Integer countryId;

  @Column(name = "country_name")
  private String countryName;

  @Column(name = "flag_name")
  private String flagName;

  @Column(name = "review_group_id")
  private Integer reviewGroupId;

  @Column(name = "review_group_name")
  private String reviewGroupName;

  @Column(name = "room_type_id")
  private Integer roomTypeId;

  @Column(name = "room_type_name")
  private String roomTypeName;

  @Column(name = "length_of_stay")
  private Integer lengthOfStay;

  @Column(name = "reviewer_reviewed_count")
  private Integer reviewerReviewedCount;

  @Column(name = "is_expert_reviewer")
  private Boolean isExpertReviewer;

  @Column(name = "is_show_global_icon")
  private Boolean isShowGlobalIcon;

  @Column(name = "is_show_reviewed_count")
  private Boolean isShowReviewedCount;

  public Long getReviewerId() { return reviewerId; }
  public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
  public String getDisplayMemberName() { return displayMemberName; }
  public void setDisplayMemberName(String displayMemberName) { this.displayMemberName = displayMemberName; }
  public Integer getCountryId() { return countryId; }
  public void setCountryId(Integer countryId) { this.countryId = countryId; }
  public String getCountryName() { return countryName; }
  public void setCountryName(String countryName) { this.countryName = countryName; }
  public String getFlagName() { return flagName; }
  public void setFlagName(String flagName) { this.flagName = flagName; }
  public Integer getReviewGroupId() { return reviewGroupId; }
  public void setReviewGroupId(Integer reviewGroupId) { this.reviewGroupId = reviewGroupId; }
  public String getReviewGroupName() { return reviewGroupName; }
  public void setReviewGroupName(String reviewGroupName) { this.reviewGroupName = reviewGroupName; }
  public Integer getRoomTypeId() { return roomTypeId; }
  public void setRoomTypeId(Integer roomTypeId) { this.roomTypeId = roomTypeId; }
  public String getRoomTypeName() { return roomTypeName; }
  public void setRoomTypeName(String roomTypeName) { this.roomTypeName = roomTypeName; }
  public Integer getLengthOfStay() { return lengthOfStay; }
  public void setLengthOfStay(Integer lengthOfStay) { this.lengthOfStay = lengthOfStay; }
  public Integer getReviewerReviewedCount() { return reviewerReviewedCount; }
  public void setReviewerReviewedCount(Integer reviewerReviewedCount) { this.reviewerReviewedCount = reviewerReviewedCount; }
  public Boolean getExpertReviewer() { return isExpertReviewer; }
  public void setExpertReviewer(Boolean expertReviewer) { isExpertReviewer = expertReviewer; }
  public Boolean getShowGlobalIcon() { return isShowGlobalIcon; }
  public void setShowGlobalIcon(Boolean showGlobalIcon) { isShowGlobalIcon = showGlobalIcon; }
  public Boolean getShowReviewedCount() { return isShowReviewedCount; }
  public void setShowReviewedCount(Boolean showReviewedCount) { isShowReviewedCount = showReviewedCount; }
}
