package com.vigulear.spring.model;

import com.vigulear.spring.validator.PersonValidator;
import com.vigulear.spring.validator.Validator;
import jakarta.persistence.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Component
@Scope("prototype")
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

  public Person setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Person setName(String name) {
    this.name = name;
    return this;
  }

  public String getSurname() {
    return surname;
  }

  public Person setSurname(String surname) {
    this.surname = surname;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Person setEmail(String email) {
    this.email = email;
    return this;
  }

  public Set<Skill> getSkills() {
    return skills;
  }

  public Person setSkills(Set<Skill> skills) {
    skills.forEach(
        skill -> {
          totalCost += (int) skill.getSkillCost();
          skill.addPerson(this);
        });
    this.skills = skills;

    return this;
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

  public Person addSkill(Skill skill) {
    if (skill.isValid() && !skills.contains(skill)) {
      skills.add(skill);
      skill.getPersons().add(this);
      totalCost += (int) skill.getSkillCost();
    }
    return this;
  }

  public Person removeSkill(Skill skill) {
    this.skills.remove(skill);
    skill.getPersons().remove(this);
    this.totalCost -= (int) skill.getSkillCost();
    return this;
  }

  public void calculateTotalCost() {
    int totalCost = 0;
    for (Skill skill : skills) {
      totalCost += (int) skill.getSkillCost();
    }
    this.totalCost = totalCost;
  }
}
