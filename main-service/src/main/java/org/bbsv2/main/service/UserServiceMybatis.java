/*
package com.example.service;

import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.util.ObjectUtil;

@Service
public class UserServiceOld {

  @Resource
  private UserMapper userMapper;


  */
/**
   * add user.
   *
   * @param user user obj
   *//*

  public void add(User user) {
    // check username, password, role
    // 1. username exist
    User dbUser = userMapper.selectByUsername(user.getUsername());
    if (dbUser != null) {
      throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
    }
    // 2. name null
    if (ObjectUtil.isEmpty(user.getName())) {
      user.setName(user.getUsername()); // set name = username
    }
    // 3. password null
    if (ObjectUtil.isEmpty(user.getPassword())) {
      user.setPassword(Constants.USER_DEFAULT_PASSWORD); // set default = 123
    }
    // 4. default role
    user.setRole(RoleEnum.USER.name());
    userMapper.insert(user);
  }

  */
/**
   * delete user.
   *
   * @param id user id
   *//*

  public void deleteById(Integer id) {
    userMapper.deleteById(id);

  }

  */
/**
   * batch delete users.
   *
   * @param ids list of user ids
   *//*

  public void deleteBatch(List<Integer> ids) {
    for (Integer id : ids) {
      this.deleteById(id);
    }
  }

  public void updateById(User user) {
    userMapper.updateById(user);
  }

  public User selectById(Integer id) {
    return userMapper.selectById(id);
  }

  public List<User> selectAll(User user) {
    return userMapper.selectAll(user);
  }

  public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    List<User> userList = userMapper.selectAll(user);
    return PageInfo.of(userList);
  }

  */
/**
   * login
   *//*

  public Account login(Account account) {
    Account dbUser = userMapper.selectByUsername(account.getUsername());
    if (ObjectUtil.isNull(dbUser)) {
      throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
    }
    if (!account.getPassword().equals(dbUser.getPassword())) {
      throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
    }
    // 生成token
    String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
    String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
    dbUser.setToken(token);
    return dbUser;
  }


  */
/**
   * register
   *//*

  public void register(Account account) {
    User user = new User();
    BeanUtils.copyProperties(account, user);
    this.add(user);
  }


  */
/**
   * 修改密码
   *//*

  public void updatePassword(Account account) {
    User dbUser = userMapper.selectByUsername(account.getUsername());
    if (ObjectUtil.isNull(dbUser)) {
      throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
    }
    if (!account.getPassword().equals(dbUser.getPassword())) {
      throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
    }
    dbUser.setPassword(account.getNewPassword());
    this.updateById(dbUser);
  }
}
*/
