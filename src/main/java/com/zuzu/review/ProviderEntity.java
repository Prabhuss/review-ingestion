package com.zuzu.review;

import jakarta.persistence.*;

@Entity
@Table(name = "providers")
public class ProviderEntity {
  @Id
  @Column(name = "provider_id")
  private Integer providerId;

  @Column(name = "provider_name", nullable = false)
  private String providerName;

  public Integer getProviderId() { return providerId; }
  public void setProviderId(Integer providerId) { this.providerId = providerId; }
  public String getProviderName() { return providerName; }
  public void setProviderName(String providerName) { this.providerName = providerName; }
}
