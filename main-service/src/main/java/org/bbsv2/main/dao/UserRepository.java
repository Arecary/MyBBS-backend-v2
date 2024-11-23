/*
package org.bbsv2.main.dao;

import org.bbsv2.main.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface  UserRepository extends JpaRepository<User, Integer> {

//第一个参数（User）：
//表示实体类类型。JpaRepository 将会管理的实体类型为 User。
//Spring Data JPA 会根据这个类型与数据库表进行映射操作。
//第二个参数（Integer）：
//表示主键的类型。JpaRepository 中定义的所有方法（如 findById、deleteById）会使用这个类型来操作主键。

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
*/
