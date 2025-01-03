package org.bbsv2.account.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;

import org.bbsv2.common.Constants;
import org.bbsv2.common.enums.ResultCodeEnum;
import org.bbsv2.common.enums.RoleEnum;
import org.bbsv2.common.exception.CustomException;
import org.bbsv2.account.dao.UserRepository;
import org.bbsv2.common.entity.Account;
import org.bbsv2.common.entity.User;
import org.bbsv2.account.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;

  private final KafkaTemplate<String, String> kafkaTemplate;


  public UserService(UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate) {
    this.userRepository = userRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * 添加用户
   */
  public void add(User user) {
    Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
    if (existingUser.isPresent()) {
      throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
    }
    if (user.getName() == null) {
      user.setName(user.getUsername()); // 设置默认姓名为用户名
    }
    if (user.getPassword() == null) {
      user.setPassword(Constants.USER_DEFAULT_PASSWORD); // 设置默认密码
    }
    user.setRole(RoleEnum.USER.name()); // 设置默认角色
    userRepository.save(user);
  }


//  /**
//   * 删除用户
//   */
//  public void deleteById(Integer id) {
//    userRepository.deleteById(id);
//  }

  /**
   * 删除用户（清理缓存）
   */
  @CacheEvict(value = "userCache", key = "#id")
  public void deleteById(Integer id) {
    userRepository.deleteById(id);

    // 发送 Kafka 消息
    Map<String, Object> message = new HashMap<>();
    message.put("userId", id);

    try {
      kafkaTemplate.send("user-delete-topic", new ObjectMapper().writeValueAsString(message));
      System.out.println("Kafka message sent: " + message);
    } catch (Exception e) {
      System.err.println("Failed to send Kafka message: " + e.getMessage());
    }
  }

//  /**
//   * 批量删除用户
//   */
//  @Transactional
//  public void deleteBatch(List<Integer> ids) {
//    ids.forEach(this::deleteById);
//  }

  /**
   * 批量删除用户（清理缓存）
   */
  @Transactional
  @CacheEvict(value = "userCache", allEntries = true)
  public void deleteBatch(List<Integer> ids) {
    ids.forEach(userRepository::deleteById); // 直接操作数据库
  }

//  /**
//   * 更新用户信息
//   */
//  public void updateById(User user) {
//    userRepository.save(user);
//  }

  /**
   * 更新用户信息（清理缓存）
   */
  @CacheEvict(value = "userCache", key = "#user.id")
  public void updateById(User user) {
    userRepository.save(user); // 更新数据库
    // 手动刷新缓存
    selectById(user.getId());
  }


//  /**
//   * 根据 ID 查询用户
//   */
//  public User selectById(Integer id) {
//    return userRepository.findById(id).orElse(null);
//  }

  /**
   * 根据 ID 查询用户 (缓存)
   */
  @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
  public User selectById(Integer id) {
    return userRepository.findById(id).orElse(null);
  }

  /**
   * 根据用户名查询用户
   */
  public Optional<User> selectByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * 分页查询用户
   */
  public PageInfo<User> selectPage(User user, int pageNum, int pageSize) {
    Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
    Page<User> page = userRepository.findByConditions(user.getUsername(), user.getName(), pageable);
    return PageInfo.of(page.getContent());
  }


  /**
   * FindAll
   */
  public List<User> selectAll(User user) {
    return userRepository.findByConditions(
            user.getUsername(),
            user.getName()
    );
  }


  /**
   * 用户登录
   */
  public Account login(Account account) {
    User dbUser = userRepository.findByUsername(account.getUsername())
            .orElseThrow(() -> new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR));

    if (!account.getPassword().equals(dbUser.getPassword())) {
      throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
    }

    // 生成 Token
    String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
    String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
    dbUser.setToken(token);
    return dbUser;
  }

  /**
   * 用户注册
   */
  public void register(Account account) {
    User user = new User();
    BeanUtils.copyProperties(account, user);
    this.add(user);
  }

  /**
   * 修改密码
   */
  public void updatePassword(Account account) {
    User dbUser = userRepository.findByUsername(account.getUsername())
            .orElseThrow(() -> new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR));

    if (!account.getPassword().equals(dbUser.getPassword())) {
      throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
    }

    dbUser.setPassword(account.getNewPassword());
    userRepository.save(dbUser);
  }

}
