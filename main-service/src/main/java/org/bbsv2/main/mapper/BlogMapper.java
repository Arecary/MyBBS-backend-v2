package org.bbsv2.main.mapper;

import org.bbsv2.main.entity.Blog;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作blog相关数据接口
 */
public interface BlogMapper {

  /**
   * 新增
   */
  int insert(Blog blog);

  /**
   * 删除
   */
  int deleteById(Integer id);

  /**
   * 修改
   */
  int updateById(Blog blog);

  /**
   * 根据ID查询
   */
  Blog selectById(Integer id);

  /**
   * 查询所有
   */
  List<Blog> selectAll(Blog blog);


  @Select("select * from blog where user_id = #{id}")
  List<Blog> selectUserBlog(Integer id);


  /**
   * 查询用户点赞收藏和评论的数据
   */
  List<Blog> selectLike(Blog blog);

  List<Blog> selectCollect(Blog blog);

  List<Blog> selectComment(Blog blog);
}
