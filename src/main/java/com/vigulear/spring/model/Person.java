package com.vigulear.spring.model;

import com.vigulear.spring.validator.PersonValidator;
import com.vigulear.spring.validator.Validator;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "persons")
public class Person {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "surname", nullable = false)
  private String surname;

  @Column(name = "email", nullable = false)
  private String email;

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(
      name = "persons_skills",
      joinColumns = @JoinColumn(name = "user_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "skill_id", nullable = false))
  private Set<Skill> skills = new HashSet<>();

  @Column(name = "total_cost", nullable = false)
  private int totalCost = 0;

  public Person() {}

  public Person(String name, String surname, String email) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.skills = new HashSet<>();
  }

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

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<Skill> getSkills() {
    return skills;
  }

  public void setSkills(Set<Skill> skills) {
    skills.forEach(
        skill -> {
          totalCost += (int) skill.getSkillCost();
          skill.addPerson(this);
        });
    this.skills = skills;
  }

  public int getTotalCost() {
    return totalCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(name, person.name)
        && Objects.equals(surname, person.surname)
        && Objects.equals(email, person.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email);
  }

  public boolean isValid() {
    Validator validator = new PersonValidator();
    return validator.isValid(this);
  }

  public void addSkill(Skill skill) {
    if (skill.isValid() && !skills.contains(skill)) {
      skills.add(skill);
      skill.getPersons().add(this);
      totalCost += (int) skill.getSkillCost();
    }
  }

  public void removeSkill(Skill skill) {
    this.skills.remove(skill);
    skill.getPersons().remove(this);
    this.totalCost -= (int) skill.getSkillCost();
  }

  public void calculateTotalCost() {
    int totalCost = 0;
    for (Skill skill : skills) {
      totalCost += (int) skill.getSkillCost();
    }
    this.totalCost = totalCost;
  }
}
