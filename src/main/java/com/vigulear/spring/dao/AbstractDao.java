package com.vigulear.spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

public abstract class AbstractDao<T> implements Dao<T> {

  private final SessionFactory sessionFactory;

  private EntityManager entityManager;

  public AbstractDao(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void persist(T t) {
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(t);

    if (entityManager != null) {
      entityManager.getTransaction().commit();
      entityManager.close();
    }
  }

  @Override
  public void persistAll(List<T> t) {
    t.forEach(this::persist);
  }

  @Override
  public T update(T t) {
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    entityManager.getTransaction().begin();

    T returnedT = null;

    try {
      returnedT = entityManager.merge(t);
      entityManager.getTransaction().commit();
      entityManager.close();
    } catch (PersistenceException exception) {
      throw new RuntimeException(exception);
    }
    return returnedT;
  }

  @Override
  public void delete(T t) {
    entityManager = sessionFactory.openSession().getEntityManagerFactory().createEntityManager();

    entityManager.getTransaction().begin();

    entityManager.remove(t);

    if (entityManager != null) {
      entityManager.getTransaction().commit();
      entityManager.close();
    }
  }

  @Override
  public void deleteAll(List<T> t) {
    t.forEach(this::delete);
  }
}
