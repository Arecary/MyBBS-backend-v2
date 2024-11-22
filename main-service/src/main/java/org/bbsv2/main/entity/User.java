package org.bbsv2.main.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class User extends Account{


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false) // 映射数据库列
  private Integer id;

  @Column(name = "username", length = 255, unique = true) // 用户名，长度 255
  private String username;

  @Column(name = "password", length = 255) // 密码
  private String password;

  @Column(name = "name", length = 255) // 姓名
  private String name;

  @Column(name = "avatar", length = 255) // 头像 URL
  private String avatar;

  @Column(name = "role", length = 255) // 角色标识
  private String role;

  @Column(name = "sex", length = 255) // 性别
  private String sex;

  @Column(name = "phone", length = 255) // 电话
  private String phone;

  @Column(name = "email", length = 255) // 邮箱
  private String email;

  @Column(name = "info", length = 255) // 简介
  private String info;

  @Column(name = "birth", length = 255) // 生日
  private String birth;

  @Transient // 非持久化字段
  private Integer blogCount;

  @Transient
  private Integer likesCount;

  @Transient
  private Integer collectCount;

}
