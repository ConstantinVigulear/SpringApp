package com.vigulear.spring.model;

import com.vigulear.spring.validator.SkillValidator;
import com.vigulear.spring.validator.Validator;
import jakarta.persistence.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Entity
@Component
@Scope("prototype")
@Table(name = "skills")
public class Skill {

  @Id
  @GeneratedValue
  @Column(name = "skill_id", nullable = false, unique = true)
  private Long id;

  @Column(name = "skill_name", nullable = false)
  private String name;

  @Column(name = "skill_domain", nullable = false)
  @Enumerated(EnumType.STRING)
  private SkillDomain domain;

  @Column(name = "skill_level", nullable = false)
  @Enumerated(EnumType.STRING)
  private SkillLevel level;

  @ManyToMany(
      mappedBy = "skills",
      fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  private Set<Person> persons = new HashSet<>();

  public Skill(String name, SkillDomain domain, SkillLevel level) {
    this.name = name;
    this.domain = domain;
    this.level = level;
  }

  public Skill() {}

  public Long getId() {
    return id;
  }

  public Skill setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Skill setName(String name) {
    this.name = name;
    return this;
  }

  public SkillDomain getDomain() {
    return domain;
  }

  public Skill setDomain(SkillDomain domain) {
    this.domain = domain;
    return this;
  }

  public SkillLevel getLevel() {
    return level;
  }

  public Skill setLevel(SkillLevel level) {
    this.level = level;
    return this;
  }

  public Set<Person> getPersons() {
    return persons;
  }

  public Skill setPersons(Set<Person> persons) {
    persons.forEach(person -> person.addSkill(this));
    this.persons = persons;
    return this;
  }

  public boolean isValid() {
    Validator validator = new SkillValidator();
    return validator.isValid(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Skill skill = (Skill) o;
    return Objects.equals(id, skill.id) && Objects.equals(name, skill.name) && domain == skill.domain && level == skill.level && Objects.equals(persons, skill.persons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, domain, level);
  }

  public Skill addPerson(Person person) {
    if (person.isValid()) {
      persons.add(person);
      person.getSkills().add(this);
    }

    return this;
  }

  public Skill removePerson(Person person) {
    this.persons.remove(person);
    person.removeSkill(this);
    return this;
  }

  public double getSkillCost() {
    return this.getDomain().getPrice() * (1 + (double) level.getLevelValue() / 10);
  }
}
