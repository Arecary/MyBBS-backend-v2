package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "activity")
public class ChangeActivity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "descr")
  private String descr;

  @Column(name = "start")
  private String start;

  @Column(name = "end")
  private String end;

  @Column(name = "form")
  private String form;

  @Column(name = "address")
  private String address;

  @Column(name = "host")
  private String host;

  @Column(name = "read_count")
  private Integer readCount;

  @Column(name = "content")
  private String content;

  @Column(name = "cover")
  private String cover;

  // Getters and Setters
  // (You can use Lombok @Data if preferred)
}
