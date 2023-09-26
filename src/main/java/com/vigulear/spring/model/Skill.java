package com.vigulear.spring.model;

import com.vigulear.spring.validator.SkillValidator;
import com.vigulear.spring.validator.Validator;
import jakarta.persistence.*;

import java.util.*;

@Entity
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

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SkillDomain getDomain() {
    return domain;
  }

  public void setDomain(SkillDomain domain) {
    this.domain = domain;
  }

  public SkillLevel getLevel() {
    return level;
  }

  public void setLevel(SkillLevel level) {
    this.level = level;
  }

  public Set<Person> getPersons() {
    return persons;
  }

  public void setPersons(Set<Person> persons) {
    persons.forEach(person -> person.addSkill(this));
    this.persons = persons;
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

  public void addPerson(Person person) {
    if (person.isValid()) {
      persons.add(person);
      person.getSkills().add(this);
    }
  }

  public void removePerson(Person person) {
    this.persons.remove(person);
    person.removeSkill(this);
  }

  public double getSkillCost() {
    return this.getDomain().getPrice() * (1 + (double) level.getLevelValue() / 10);
  }
}
