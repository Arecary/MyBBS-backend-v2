package org.bbsv2.main.service;

import org.bbsv2.common.entity.User;
import org.bbsv2.common.enums.RoleEnum;
import org.bbsv2.common.entity.Account;
import org.bbsv2.main.client.UserFeignClient;
import org.bbsv2.main.entity.Comment;
import org.bbsv2.main.mapper.CommentMapper;
import org.bbsv2.main.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;

/**
 * 业务处理
 **/
@Service
public class CommentService {

  @Resource
  private CommentMapper commentMapper;

  @Resource
  private UserFeignClient userFeignClient;

  /**
   * 新增
   */
  public void add(Comment comment) {
    Account currentUser = TokenUtils.getCurrentUser();
    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
      comment.setUserId(currentUser.getId());
    }
    comment.setTime(DateUtil.now());
    commentMapper.insert(comment); // 先插入数据 拿到主键ID 再设置数据
    if (comment.getRootId() == null) {
      comment.setRootId(comment.getId());
      commentMapper.updateById(comment); // 注意更新一下rootID
    }

  }

  /**
   * 删除
   */
  public void deleteById(Integer id) {
    commentMapper.deleteById(id);
  }

  /**
   * 批量删除
   */
  public void deleteBatch(List<Integer> ids) {
    for (Integer id : ids) {
      commentMapper.deleteById(id);
    }
  }

  /**
   * 修改
   */
  public void updateById(Comment comment) {
    commentMapper.updateById(comment);
  }

  /**
   * 根据ID查询
   */
  public Comment selectById(Integer id) {
//    return commentMapper.selectById(id);

    Comment comment = commentMapper.selectById(id);
    if (comment != null) {
      // 使用 UserFeignClient 查询用户信息
      User user = userFeignClient.getUserById(comment.getUserId());
      if (user != null) {
        comment.setUserName(user.getUsername());
        comment.setAvatar(user.getAvatar());
      }
    }
    return comment;
  }

  /**
   * 查询所有
   */
  public List<Comment> selectAll(Comment comment) {
//    return commentMapper.selectAll(comment);
    List<Comment> commentList = commentMapper.selectAll(comment);
    return commentList.stream().map(this::populateUserInfo).collect(Collectors.toList());
  }

  /**
   * 分页查询
   */
  public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
    // Step 1: 使用 PageHelper 启动分页
    PageHelper.startPage(pageNum, pageSize);

    // Step 2: 查询分页后的评论列表
    List<Comment> list = commentMapper.selectAll(comment);

    // Step 3: 包装成 PageInfo 对象，确保分页信息
    PageInfo<Comment> pageInfo = PageInfo.of(list);

    // Step 4: 对每个评论进行用户信息的注入
    List<Comment> updatedList = pageInfo.getList().stream()
            .map(this::populateUserInfo)
            .collect(Collectors.toList());
    pageInfo.setList(updatedList);

    return pageInfo;
  }
//  public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
//    PageHelper.startPage(pageNum, pageSize);
//    List<Comment> list = commentMapper.selectAll(comment);
//    return PageInfo.of(list);
//  }

  /**
   * 查询前台展示的博客信息
   */
  public List<Comment> selectForUser(Comment comment) {
    List<Comment> commentList = commentMapper.selectForUser(comment); // 查询一级的评论
    for (Comment c : commentList) { // 查询回复列表
      Comment param = new Comment();
      param.setRootId(c.getId());
      List<Comment> children = this.selectAll(param);
      children = children.stream().filter(child -> !child.getId().equals(c.getId())).collect(Collectors.toList()); // 排除当前查询结果里最外层节点
      c.setChildren(children);
    }
    return commentList.stream().map(this::populateUserInfo).collect(Collectors.toList());
  }
//  public List<Comment> selectForUser(Comment comment) {
//    List<Comment> commentList = commentMapper.selectForUser(comment);  // 查询一级的评论
//    for (Comment c : commentList) {  // 查询回复列表
//      Comment param = new Comment();
//      param.setRootId(c.getId());
//      List<Comment> children = this.selectAll(param);
//      children = children.stream().filter(child -> !child.getId().equals(c.getId())).collect(Collectors.toList());  // 排除当前查询结果里最外层节点
//      c.setChildren(children);
//    }
//    return commentList;
//  }

  public Integer selectCount(Integer fid, String module) {
    return commentMapper.selectCount(fid, module);
  }


  /**
   * delete By UserId
   * Use in kafka
   */
  public void deleteByUserId(Integer userId) {
    List<Comment> comments = commentMapper.selectAllByUserId(userId);
    for (Comment comment : comments) {
      this.deleteById(comment.getId());
    }
  }

  private Comment populateUserInfo(Comment comment) {
    if (comment.getUserId() != null) {
      User user = userFeignClient.getUserById(comment.getUserId());
      if (user != null) {
        comment.setUserName(user.getUsername());
        comment.setAvatar(user.getAvatar());
      }
    }
    return comment;
  }
}