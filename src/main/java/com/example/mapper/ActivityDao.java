package com.example.dao;

import com.example.entity.Activity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ActivityDao {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void add(Activity activity) {
    entityManager.persist(activity);
  }

  @Transactional
  public void deleteById(Integer id) {
    Activity activity = entityManager.find(Activity.class, id);
    if (activity != null) {
      entityManager.remove(activity);
    }
  }

  @Transactional
  public void updateById(Activity activity) {
    entityManager.merge(activity);
  }

  public Activity selectById(Integer id) {
    return entityManager.find(Activity.class, id);
  }

  public List<Activity> selectAll() {
    return entityManager.createQuery("from Activity", Activity.class).getResultList();
  }
}
