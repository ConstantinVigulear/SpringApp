package com.vigulear.spring.model;

import java.util.Arrays;

public enum SkillLevel {
  NONE(0),
  LOW(1),
  MEDIUM(2),
  HIGH(3),
  ADVANCED(4),
  GODLIKE(5);

  private final int levelValue;

  SkillLevel(int value) {
    this.levelValue = value;
  }

  public int getLevelValue() {
    return levelValue;
  }

  public static SkillLevel getSkillLevelByValue(int levelValue) {
    return Arrays.stream(SkillLevel.values())
            .filter(level -> level.getLevelValue() == levelValue)
            .findAny()
            .orElse(SkillLevel.NONE);
  }

  public static SkillLevel getSkillLevelByName(String levelName) {
    return Arrays.stream(SkillLevel.values())
            .filter(e -> e.name().equals(levelName.toUpperCase()))
            .findFirst()
            .orElse(SkillLevel.NONE);
  }
}
