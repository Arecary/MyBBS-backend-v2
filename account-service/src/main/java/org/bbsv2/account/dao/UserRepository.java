package org.bbsv2.account.dao;

import org.bbsv2.account.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  // 自定义查询方法
  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u WHERE " +
          "(:username IS NULL OR u.username = :username) AND " +
          "(:name IS NULL OR u.name LIKE CONCAT('%', :name, '%')) " +
          "ORDER BY u.id DESC")
  Page<User> findByConditions(@Param("username") String username, @Param("name") String name, Pageable pageable);


  @Query("SELECT u FROM User u WHERE " +
          "(:username IS NULL OR u.username = :username) AND " +
          "(:name IS NULL OR u.name LIKE CONCAT('%', :name, '%')) " +
          "ORDER BY u.id DESC")
  List<User> findByConditions(@Param("username") String username, @Param("name") String name);


}
