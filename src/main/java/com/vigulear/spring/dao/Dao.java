package com.vigulear.spring.dao;

import java.util.List;

public interface Dao<T> {

  T get(Long id);

  T get(T t);

  List<T> getAll();

  void persist(T t);

  void persistAll(List<T> t);

  T update(T t);

  void delete(T t);

  void deleteAll(List<T> t);

}
