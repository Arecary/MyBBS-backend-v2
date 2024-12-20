package org.bbsv2.main.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import org.bbsv2.common.enums.LikesModuleEnum;
import org.bbsv2.common.enums.RoleEnum;
import org.bbsv2.common.entity.Account;
import org.bbsv2.common.entity.User;

import org.bbsv2.main.client.UserFeignClient;
import org.bbsv2.main.entity.Blog;
import org.bbsv2.main.entity.Collect;
import org.bbsv2.main.entity.Likes;
import org.bbsv2.main.mapper.BlogMapper;
import org.bbsv2.main.mapper.CommentMapper;
import org.bbsv2.main.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 博客信息业务处理
 **/
@Service
public class BlogService {

  @Resource
  private BlogMapper blogMapper;

  @Resource
  private CommentMapper commentMapper;

  @Resource
  private UserFeignClient userFeignClient;

  @Resource
  LikesService likesService;

  @Resource
  CollectService collectService;

  /**
   * 新增
   */
  public void add(Blog blog) {
    blog.setDate(DateUtil.today());
    Account currentUser = TokenUtils.getCurrentUser();
    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
      blog.setUserId(currentUser.getId());
    }
    blogMapper.insert(blog);
  }

  /**
   * 删除
   */
  public void deleteById(Integer id) {
    blogMapper.deleteById(id);
  }

  /**
   * 批量删除
   */
  public void deleteBatch(List<Integer> ids) {
    for (Integer id : ids) {
      blogMapper.deleteById(id);
    }
  }

  /**
   * 修改
   */
  public void updateById(Blog blog) {
    blogMapper.updateById(blog);
  }

  /**
   * 根据ID查询
   */
  public Blog selectById(Integer id) {
//    Blog blog = blogMapper.selectById(id);
//
//    User user = userFeignClient.getUserById(blog.getUserId());
//    List<Blog> userBlogList = blogMapper.selectUserBlog(user.getId());
//    user.setBlogCount(userBlogList.size());
//    //  当前用户收到的点赞和收藏的数据
//    int userLikesCount = 0;
//    int userCollectCount = 0;
//    for (Blog b : userBlogList) {
//      Integer fid = b.getId();
//      int likesCount = likesService.selectByFidAndModule(fid, LikesModuleEnum.BLOG.getValue());
//      userLikesCount += likesCount;
//
//      int collectCount = collectService.selectByFidAndModule(fid, LikesModuleEnum.BLOG.getValue());
//      userCollectCount += collectCount;
//    }
//    user.setLikesCount(userLikesCount);
//    user.setCollectCount(userCollectCount);
//
//
//    blog.setUser(user);  // 设置作者信息
//    // 查询当前博客的点赞数据
//    int likesCount = likesService.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());
//    blog.setLikesCount(likesCount);
//    Likes userLikes = likesService.selectUserLikes(id, LikesModuleEnum.BLOG.getValue());
//    blog.setUserLike(userLikes != null);
//
//    // 查询当前博客的收藏数据
//    int collectCount = collectService.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());
//    blog.setCollectCount(collectCount);
//    Collect userCollect = collectService.selectUserCollect(id, LikesModuleEnum.BLOG.getValue());
//    blog.setUserCollect(userCollect != null);
//
//    // 更新博客views数据
//    blog.setReadCount(blog.getReadCount() + 1);
//    this.updateById(blog);
//    return blog;
    // 查询博客信息
    Blog blog = blogMapper.selectById(id);
    if (blog != null && blog.getUserId() != null) {
      // 使用 FeignClient 查询用户信息
      User user = userFeignClient.getUserById(blog.getUserId());
      if (user != null) {
        // 查询用户的博客列表
        List<Blog> userBlogList = blogMapper.selectUserBlog(user.getId());
        user.setBlogCount(userBlogList.size());

        // 当前用户收到的点赞和收藏数量统计
        int userLikesCount = 0;
        int userCollectCount = 0;

        for (Blog b : userBlogList) {
          Integer fid = b.getId();

          // 计算用户的总点赞数
          int likesCount = likesService.selectByFidAndModule(fid, LikesModuleEnum.BLOG.getValue());
          userLikesCount += likesCount;

          // 计算用户的总收藏数
          int collectCount = collectService.selectByFidAndModule(fid, LikesModuleEnum.BLOG.getValue());
          userCollectCount += collectCount;
        }

        user.setLikesCount(userLikesCount);
        user.setCollectCount(userCollectCount);
        blog.setUser(user);  // 设置作者信息
      }
    }

    // 查询当前博客的点赞数据
    int likesCount = likesService.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());
    blog.setLikesCount(likesCount);
    Likes userLikes = likesService.selectUserLikes(id, LikesModuleEnum.BLOG.getValue());
    blog.setUserLike(userLikes != null);

    // 查询当前博客的收藏数据
    int collectCount = collectService.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());
    blog.setCollectCount(collectCount);
    Collect userCollect = collectService.selectUserCollect(id, LikesModuleEnum.BLOG.getValue());
    blog.setUserCollect(userCollect != null);

    // 更新博客阅读数
    blog.setReadCount(blog.getReadCount() + 1);
    this.updateById(blog);

    return blog;

  }

  /**
   * 查询所有
   */
  public List<Blog> selectAll(Blog blog) {
//    return blogMapper.selectAll(blog);
    List<Blog> blogList = blogMapper.selectAll(blog);
    return blogList.stream().map(this::populateUserInfo).collect(Collectors.toList());
  }

  /**
   * 分页查询
   */
  public PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize) {
//    PageHelper.startPage(pageNum, pageSize);
//    List<Blog> list = blogMapper.selectAll(blog);
//    for (Blog b : list) {
//      int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
//      b.setLikesCount(likesCount);
//    }
//    return PageInfo.of(list);
    // Step 1: 使用 PageHelper 启动分页
    PageHelper.startPage(pageNum, pageSize);

    // Step 2: 查询分页后的博客列表
    List<Blog> list = blogMapper.selectAll(blog);

    // Step 3: 包装成 PageInfo 对象，确保分页信息
    PageInfo<Blog> pageInfo = PageInfo.of(list);

    // Step 4: 对每个博客进行用户信息的注入
    List<Blog> updatedList = pageInfo.getList().stream()
            .map(this::populateUserInfo)
            .collect(Collectors.toList());

    // Step 5: 使用注入用户信息后的列表更新 PageInfo 对象
    pageInfo.setList(updatedList);

    return pageInfo;
  }

  /**
   * 博客榜单
   */
  public List<Blog> selectTop() {
    List<Blog> blogList = this.selectAll(null);
    blogList = blogList.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
            .limit(20)
            .collect(Collectors.toList());
    return blogList;
  }

  /**
   * 博客推荐
   * 基于博客的标签（tags）来推荐相关博客
   */
  public Set<Blog> selectRecommend(Integer blogId) {
    Blog blog = this.selectById(blogId);
    String tags = blog.getTags();
    Set<Blog> blogSet = new HashSet<>();
    if (ObjectUtil.isNotEmpty(tags)) {
      List<Blog> blogList = this.selectAll(null);
      JSONArray tagsArr = JSONUtil.parseArray(tags);
      for (Object tag : tagsArr) {
        // 筛选出包含当前博客标签的其他的博客列表
        Set<Blog> collect = blogList.stream().filter(b -> b.getTags().contains(tag.toString()) && !blogId.equals(b.getId()))
                .collect(Collectors.toSet());
        blogSet.addAll(collect);
      }
    }

    blogSet = blogSet.stream().limit(5).collect(Collectors.toSet());
    blogSet.forEach(b ->{
      int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
      b.setLikesCount(likesCount);
    });

    return blogSet;
  }


  /**
   * 分页查询当前用户的博客列表
   */
  public PageInfo<Blog> selectUser(Blog blog, Integer pageNum, Integer pageSize) {
//    Account currentUser = TokenUtils.getCurrentUser();
//    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
//      blog.setUserId(currentUser.getId());
//    }
//
//    return this.selectPage(blog, pageNum, pageSize);

    Account currentUser = TokenUtils.getCurrentUser();
    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
      blog.setUserId(currentUser.getId());
    }
    PageHelper.startPage(pageNum, pageSize);
    List<Blog> list = blogMapper.selectUserBlog(currentUser.getId());
    list = list.stream().map(this::populateUserInfo).collect(Collectors.toList());
    return PageInfo.of(list);
  }

  /**
   * 查询用户点赞收藏和评论的数据
   */
  public PageInfo<Blog> selectLike(Blog blog, Integer pageNum, Integer pageSize) {
//    Account currentUser = TokenUtils.getCurrentUser();
//    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
//      blog.setUserId(currentUser.getId());
//    }
//    PageHelper.startPage(pageNum, pageSize);
//    List<Blog> list = blogMapper.selectLike(blog);
//    PageInfo<Blog> pageInfo = PageInfo.of(list);
//    List<Blog> blogList = pageInfo.getList();
//    for (Blog b : blogList) {
//      int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
//      b.setLikesCount(likesCount);
//    }
//    return pageInfo;

    Account currentUser = TokenUtils.getCurrentUser();
    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
      blog.setUserId(currentUser.getId());
    }
    PageHelper.startPage(pageNum, pageSize);
    List<Blog> list = blogMapper.selectLike(blog);
    list = list.stream().map(this::populateUserInfo).collect(Collectors.toList());
    PageInfo<Blog> pageInfo = PageInfo.of(list);
    return pageInfo;
  }

  public PageInfo<Blog> selectCollect(Blog blog, Integer pageNum, Integer pageSize) {
//    Account currentUser = TokenUtils.getCurrentUser();
//    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
//      blog.setUserId(currentUser.getId());
//    }
//    PageHelper.startPage(pageNum, pageSize);
//    List<Blog> list = blogMapper.selectCollect(blog);
//    PageInfo<Blog> pageInfo = PageInfo.of(list);
//    List<Blog> blogList = pageInfo.getList();
//    for (Blog b : blogList) {
//      int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
//      b.setLikesCount(likesCount);
//    }
//    return pageInfo;

    Account currentUser = TokenUtils.getCurrentUser();
    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
      blog.setUserId(currentUser.getId());
    }
    PageHelper.startPage(pageNum, pageSize);
    List<Blog> list = blogMapper.selectCollect(blog);
    list = list.stream().map(this::populateUserInfo).collect(Collectors.toList());
    PageInfo<Blog> pageInfo = PageInfo.of(list);
    return pageInfo;
  }

  public PageInfo<Blog> selectComment(Blog blog, Integer pageNum, Integer pageSize) {
//    Account currentUser = TokenUtils.getCurrentUser();
//    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
//      blog.setUserId(currentUser.getId());
//    }
//    PageHelper.startPage(pageNum, pageSize);
//    List<Blog> list = blogMapper.selectComment(blog);
//    PageInfo<Blog> pageInfo = PageInfo.of(list);
//    List<Blog> blogList = pageInfo.getList();
//    for (Blog b : blogList) {
//      int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
//      b.setLikesCount(likesCount);
//    }
//    return pageInfo;

    Account currentUser = TokenUtils.getCurrentUser();
    if (RoleEnum.USER.name().equals(currentUser.getRole())) {
      blog.setUserId(currentUser.getId());
    }
    PageHelper.startPage(pageNum, pageSize);
    List<Blog> list = blogMapper.selectComment(blog);
    list = list.stream().map(this::populateUserInfo).collect(Collectors.toList());
    PageInfo<Blog> pageInfo = PageInfo.of(list);
    return pageInfo;
  }


  /**
   * delete By UserId
   * Use in kafka
   */
  public void deleteByUserId(Integer userId) {
    List<Blog> blogs = blogMapper.selectAllByUserId(userId);
    for (Blog blog : blogs) {
      this.deleteById(blog.getId());
    }
  }

  // 删除博客下所有comments
  // 声明方法或类中的数据库操作具有事务性 保证ACID原则
  @Transactional
  public void deleteBlogsAndCommentsByUserId(Integer userId) {
    // 查询用户的所有博客
    List<Integer> blogIds = blogMapper.selectBlogIdsByUserId(userId);

    if (!blogIds.isEmpty()) {
      // 删除博客下的所有评论
      commentMapper.deleteByBlogIds(blogIds);

      // 删除用户的所有博客
      blogMapper.deleteByUserId(userId);
    }
  }

  private Blog populateUserInfo(Blog blog) {
    if (blog.getUserId() != null) {
      User user = userFeignClient.getUserById(blog.getUserId());
      if (user != null) {
        blog.setUser(user);
      }
    }
    return blog;
  }

}
