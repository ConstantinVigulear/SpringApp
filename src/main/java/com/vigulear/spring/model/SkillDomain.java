package com.vigulear.spring.model;

import java.util.Arrays;

public enum SkillDomain {
  SECURITY(150),
  DATA_ANALYSIS(160),
  DEVOPS(170),
  CLOUD_COMPUTING(180),
  MACHINE_LEARNING(140),
  PROGRAMMING(200),
  SYSTEMS_NETWORKS(140),
  NONE(0);

  private final int price;

  SkillDomain(int price) {
    this.price = price;
  }

  public int getPrice() {
    return price;
  }

  public static SkillDomain getSkillDomainByName(String domainName) {
    return Arrays.stream(SkillDomain.values())
            .filter(e -> e.name().equals(domainName.toUpperCase()))
            .findFirst()
            .orElse(SkillDomain.NONE);
  }

}
