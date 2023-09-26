package com.vigulear.spring.service;

import com.vigulear.spring.dao.Dao;

import java.util.List;
import com.vigulear.spring.model.Skill;
import jakarta.transaction.Transactional;

@Transactional
public class SkillServiceImpl implements SkillService {

  private final Dao<Skill> skillDao;

  public SkillServiceImpl(Dao<Skill> skillDao) {
    this.skillDao = skillDao;
  }

  @Override
  public void persist(Skill entity) {
    skillDao.persist(entity);
  }

  @Override
  public void persistAll(List<Skill> entities) {
    skillDao.persistAll(entities);
  }

  @Override
  public Skill update(Skill entity) {
    return skillDao.update(entity);
  }

  @Override
  public Skill findById(Long id) {
    return skillDao.get(id);
  }

  @Override
  public Skill findByEntity(Skill skill) {
    return skillDao.get(skill);
  }

  @Override
  public void delete(Skill entity) {
    skillDao.delete(entity);
  }

  @Override
  public List<Skill> findAll() {
    return skillDao.getAll();
  }

  @Override
  public void deleteAll(List<Skill> entities) {
    skillDao.deleteAll(entities);
  }
}
