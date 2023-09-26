package com.vigulear.spring.service;

import java.util.List;

public interface Service<T> {

    void persist(T entity);

    void persistAll(List<T> entities);

    T update(T entity);

    T findById(Long id);
    T findByEntity(T t);

    void delete(T entity);

    List<T> findAll();

    void deleteAll(List<T> entities);
}
