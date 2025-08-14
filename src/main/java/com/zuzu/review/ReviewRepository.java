package com.zuzu.review;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.function.Function;

public class ReviewRepository {
  private static final Logger log = LoggerFactory.getLogger(ReviewRepository.class);
  private final EntityManagerFactory emf;
  public ReviewRepository(EntityManagerFactory emf){ this.emf = emf; }

  private EntityManager em(){ return emf.createEntityManager(); }

  /**
   * Execute all operations within a single transaction to ensure atomicity and proper resource management
   */
  public <T> T processInTransaction(Function<EntityManager, T> operation) throws Exception {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      T result = operation.apply(em);
      tx.commit();
      return result;
    } catch (Exception ex) {
      if (tx.isActive()) tx.rollback();
      throw ex;
    } finally {
      em.close();
    }
  }

  public void upsertHotel(EntityManager em, Long hotelId, String hotelName){
    if (hotelId == null) return;
    HotelEntity h = em.find(HotelEntity.class, hotelId);
    if (h == null) {
      h = new HotelEntity();
      h.setHotelId(hotelId);
      h.setHotelName(hotelName != null ? hotelName : ("Hotel " + hotelId));
      em.persist(h);
    } else {
      if (hotelName != null && !hotelName.equals(h.getHotelName())) h.setHotelName(hotelName);
    }
  }


  public void upsertProvider(EntityManager em, Integer providerId, String providerName){
    if (providerId == null) return;
    ProviderEntity p = em.find(ProviderEntity.class, providerId);
    if (p == null) {
      p = new ProviderEntity();
      p.setProviderId(providerId);
      p.setProviderName(providerName != null ? providerName : ("Provider " + providerId));
      em.persist(p);
    } else {
      if (providerName != null && !providerName.equals(p.getProviderName())) p.setProviderName(providerName);
    }
  }


  public Long insertReviewer(EntityManager em, ReviewerEntity r){
    em.persist(r);
    return r.getReviewerId();
  }


  public Long upsertReview(EntityManager em, ReviewEntity e){
    ReviewId reviewId = new ReviewId(e.getHotelReviewId(), e.getProviderId());
    ReviewEntity existing = em.find(ReviewEntity.class, reviewId);
    if (existing == null) {
      em.persist(e);
    } else {
      // Update mutable fields
      existing.setHotelId(e.getHotelId());
      existing.setProviderId(e.getProviderId());
      existing.setReviewerId(e.getReviewerId());
      existing.setRating(e.getRating());
      existing.setRatingText(e.getRatingText());
      existing.setCheckInMonthYear(e.getCheckInMonthYear());
      existing.setReviewDate(e.getReviewDate());
      existing.setReviewTitle(e.getReviewTitle());
      existing.setReviewComments(e.getReviewComments());
      existing.setReviewNegatives(e.getReviewNegatives());
      existing.setReviewPositives(e.getReviewPositives());
      existing.setEncryptedReviewData(e.getEncryptedReviewData());
      existing.setResponderName(e.getResponderName());
      existing.setResponseDateText(e.getResponseDateText());
      existing.setResponseTranslateSource(e.getResponseTranslateSource());
      existing.setResponseText(e.getResponseText());
      existing.setTranslateSource(e.getTranslateSource());
      existing.setTranslateTarget(e.getTranslateTarget());
      existing.setShowReviewResponse(e.getShowReviewResponse());
      existing.setOriginalTitle(e.getOriginalTitle());
      existing.setOriginalComment(e.getOriginalComment());
      existing.setFormattedResponseDate(e.getFormattedResponseDate());
      existing.setReviewProviderText(e.getReviewProviderText());
    }
    return e.getHotelReviewId();
  }


  public void upsertOverall(EntityManager em, HotelProviderOverallEntity e){
    HotelProviderOverallEntity existing = em.createQuery(
      "select o from HotelProviderOverallEntity o where o.hotelId=:hid and o.providerId=:pid",
      HotelProviderOverallEntity.class)
      .setParameter("hid", e.getHotelId())
      .setParameter("pid", e.getProviderId())
      .setMaxResults(1)
      .getResultStream().findFirst().orElse(null);
    if (existing == null) {
      em.persist(e);
    } else {
      existing.setOverallScore(e.getOverallScore());
      existing.setReviewCount(e.getReviewCount());
    }
  }


  public void upsertGrade(EntityManager em, HotelProviderGradeEntity e){
    HotelProviderGradeEntity existing = em.createQuery(
      "select g from HotelProviderGradeEntity g where g.hotelId=:hid and g.providerId=:pid and g.categoryName=:cat",
      HotelProviderGradeEntity.class)
      .setParameter("hid", e.getHotelId())
      .setParameter("pid", e.getProviderId())
      .setParameter("cat", e.getCategoryName())
      .setMaxResults(1)
      .getResultStream().findFirst().orElse(null);
    if (existing == null) {
      em.persist(e);
    } else {
      existing.setScore(e.getScore());
    }
  }



  public List<ReviewEntity> findByHotelId(long hotelId){
    EntityManager em = em();
    try {
      return em.createQuery("from ReviewEntity r where r.hotelId = :hid order by r.reviewDate desc", ReviewEntity.class)
               .setParameter("hid", hotelId)
               .setMaxResults(50)
               .getResultList();
    } finally { em.close(); }
  }



  public Long findExistingReviewerId(EntityManager em, ReviewerEntity r){
    return em.createQuery(
      "select rv.reviewerId from ReviewerEntity rv " +
      "where coalesce(rv.displayMemberName,'') = coalesce(:dn,'') " +
      "and coalesce(rv.countryId, -1) = coalesce(:cid, -1) " +
      "and coalesce(rv.reviewGroupId, -1) = coalesce(:rgid, -1) " +
      "and coalesce(rv.roomTypeId, -1) = coalesce(:rtid, -1) " +
      "and coalesce(rv.lengthOfStay, -1) = coalesce(:los, -1)",
      Long.class)
      .setParameter("dn", r.getDisplayMemberName())
      .setParameter("cid", r.getCountryId())
      .setParameter("rgid", r.getReviewGroupId())
      .setParameter("rtid", r.getRoomTypeId())
      .setParameter("los", r.getLengthOfStay())
      .setMaxResults(1)
      .getResultStream().findFirst().orElse(null);
  }


  public Long upsertReviewer(EntityManager em, ReviewerEntity r){
    Long existingId = findExistingReviewerId(em, r);
    if (existingId != null) return existingId;
    return insertReviewer(em, r);
  }

}

