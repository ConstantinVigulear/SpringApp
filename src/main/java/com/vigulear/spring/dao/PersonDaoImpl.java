package com.vigulear.spring.dao;

import com.vigulear.spring.model.Person;
import com.vigulear.spring.model.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonDaoImpl extends AbstractDao<Person> implements PersonDao {

  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public Person get(Long id) {
    Person person = entityManager.find(Person.class, id);
    person.getSkills().size();
      return person;
  }

  @Override
  public Person get(Person person) {

    Person resultPerson = null;

    String hql = "FROM Person P WHERE P.name = :name and P.surname = :surname and P.email = :email";
    Query query = entityManager.createQuery(hql);
    query.setParameter("name", person.getName());
    query.setParameter("surname", person.getSurname());
    query.setParameter("email", person.getEmail());

    if (!query.getResultList().isEmpty()) {
      resultPerson = (Person) query.getResultList().get(0);
      resultPerson.getSkills().size();
    }

    return resultPerson;
  }

  @Override
  public List<Person> getAll() {
      return entityManager.createQuery("from Person ", Person.class).getResultList();
  }

  @Override
  public void persist(Person person) {
    if (get(person) == null && person.isValid()) {
      super.persist(person);
    }
  }

  @Override
  public void persistAll(List<Person> t) {
    super.persistAll(t);
  }

  @Override
  public Person update(Person person) {
    return super.update(person);
  }

  @Override
  public void delete(Person person) {
    List<Skill> skills = new ArrayList<>(person.getSkills());

    for (Skill skill : skills) {
      skill.removePerson(person);
    }
    super.delete(person);
  }

  @Override
  public void deleteAll(List<Person> t) {
    super.deleteAll(t);
  }
}
