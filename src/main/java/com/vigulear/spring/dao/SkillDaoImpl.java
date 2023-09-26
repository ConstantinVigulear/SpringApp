package com.vigulear.spring.dao;

import com.vigulear.spring.model.Person;
import com.vigulear.spring.model.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class SkillDaoImpl extends AbstractDao<Skill> implements SkillDao {

  private final SessionFactory sessionFactory;

  private EntityManager entityManager;

  public SkillDaoImpl(SessionFactory sessionFactory, SessionFactory sessionFactory1) {
    super(sessionFactory);
    this.sessionFactory = sessionFactory1;
  }

  @Override
  public Skill get(Long id) {

    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    Skill skillToFind = entityManager.find(Skill.class, id);
    entityManager.close();

    return skillToFind;
  }

  @Override
  public Skill get(Skill skill) {
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    Skill resultSkill = null;

    String hql = "FROM Skill S WHERE S.name = :name and S.domain = :domain and S.level = :level";
    Query query = entityManager.createQuery(hql);
    query.setParameter("name", skill.getName());
    query.setParameter("domain", skill.getDomain());
    query.setParameter("level", skill.getLevel());

    if (!query.getResultList().isEmpty()) {
      resultSkill = (Skill) query.getResultList().get(0);
    }

    entityManager.close();

    return resultSkill;
  }

  @Override
  public List<Skill> getAll() {
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    List<Skill> skills = entityManager.createQuery("from Skill ", Skill.class).getResultList();

    entityManager.close();

    return skills;
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
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    removeSkillFromLocalPeople(skill);

    Skill skillToDelete = get(skill);

    entityManager.getTransaction().begin();

    List<Person> people = new ArrayList<>(skillToDelete.getPersons());

    removeSkillFromRemotePeople(skill, people);

    if (entityManager != null) {
      entityManager.getTransaction().commit();
      entityManager.close();
    }

    super.delete(skill);
  }

  private void updatePeople(List<Person> people) {
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

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
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

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
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    TypedQuery<Person> query =
            entityManager.createQuery("select p from Person p " + "where p.id = :id", Person.class);
    query.setParameter("id", person.getId());
    return query;
  }
}
