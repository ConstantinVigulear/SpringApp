package com.vigulear.spring.dao;

import com.vigulear.spring.model.Person;
import com.vigulear.spring.model.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SkillDaoImpl extends AbstractDao<Skill> implements SkillDao {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Skill get(Long id) {
      return entityManager.find(Skill.class, id);
  }

  @Override
  public Skill get(Skill skill) {

    Skill resultSkill = null;

    String hql = "FROM Skill S WHERE S.name = :name and S.domain = :domain and S.level = :level";
    Query query = entityManager.createQuery(hql);
    query.setParameter("name", skill.getName());
    query.setParameter("domain", skill.getDomain());
    query.setParameter("level", skill.getLevel());

    if (!query.getResultList().isEmpty()) {
      resultSkill = (Skill) query.getResultList().get(0);
    }

    return resultSkill;
  }

  @Override
  public List<Skill> getAll() {
      return entityManager.createQuery("from Skill ", Skill.class).getResultList();
  }

  @Override
  public void persist(Skill skill) {

    if (get(skill) == null && skill.isValid()) {
      super.persist(skill);
    }
  }

  @Override
  public Skill update(Skill skill) {
    Skill updatedSkill = super.update(skill);

    List<Person> people = new ArrayList<>(skill.getPersons());

    updatePeople(people);

    return updatedSkill;
  }

  @Override
  public void delete(Skill skill) {

    removeSkillFromLocalPeople(skill);

    Skill skillToDelete = get(skill);

    List<Person> people = new ArrayList<>(skillToDelete.getPersons());

    removeSkillFromRemotePeople(skill, people);

    super.delete(skill);
  }

  private void updatePeople(List<Person> people) {

    people.forEach(
            person -> {
              int oldTotalCost = person.getTotalCost();
              person.calculateTotalCost();
              int newTotalCost = person.getTotalCost();

              if (oldTotalCost != newTotalCost) {
                entityManager.merge(person);
              }
            }
    );
  }

  private void removeSkillFromRemotePeople(Skill skill, List<Person> people) {

    people.forEach(
        person -> {
          TypedQuery<Person> queryPersonFromDataBase = getPersonFromDataBase(person);

          if (!queryPersonFromDataBase.getResultList().isEmpty()) {

            Person foundPerson = queryPersonFromDataBase.getResultList().get(0);
            foundPerson.removeSkill(skill);
            entityManager.merge(foundPerson);
          }
        });
  }

  private static void removeSkillFromLocalPeople(Skill skill) {
    (new ArrayList<>(skill.getPersons())).forEach(person -> person.removeSkill(skill));
  }

  private TypedQuery<Person> getPersonFromDataBase(Person person) {

    TypedQuery<Person> query =
            entityManager.createQuery("select p from Person p " + "where p.id = :id", Person.class);
    query.setParameter("id", person.getId());
    return query;
  }
}
