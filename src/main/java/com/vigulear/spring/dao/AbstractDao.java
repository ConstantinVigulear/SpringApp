package com.vigulear.spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.hibernate.SessionFactory;

import java.util.List;

public abstract class AbstractDao<T> implements Dao<T> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void persist(T t) {
    entityManager.persist(t);
  }

  @Override
  public void persistAll(List<T> t) {
    t.forEach(this::persist);
  }

  @Override
  public T update(T t) {

    T returnedT;

      returnedT = entityManager.merge(t);

    return returnedT;
  }

  @Override
  public void delete(T t) {

    entityManager.remove(t);

  }

  @Override
  public void deleteAll(List<T> t) {
    t.forEach(this::delete);
  }
}
