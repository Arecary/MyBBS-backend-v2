package org.bbsv2.main.mapper;

import org.apache.ibatis.annotations.Delete;
import org.bbsv2.main.entity.Comment;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作comment相关数据接口
 */
public interface CommentMapper {

  /**
   * 新增
   */
  int insert(Comment comment);

  /**
   * 删除
   */
  int deleteById(Integer id);

  /**
   * 修改
   */
  int updateById(Comment comment);

  /**
   * 根据ID查询
   */
  Comment selectById(Integer id);

  /**
   * 查询所有
   */
  List<Comment> selectAll(Comment comment);


  /**
   * 查询前台展示的博客信息
   */
  List<Comment> selectForUser(Comment comment);

  @Select("select count(*) from comment where fid = #{fid} and module = #{module}")
  Integer selectCount(@Param("fid") Integer fid, @Param("module") String module);

  @Select("SELECT * FROM comment WHERE user_id = #{userId}")
  List<Comment> selectAllByUserId(Integer userId);


  @Delete("<script>" +
          "DELETE FROM comment WHERE fid IN " +
          "<foreach item='id' collection='blogIds' open='(' separator=',' close=')'>" +
          "#{id}" +
          "</foreach>" +
          "</script>")
  void deleteByBlogIds(@Param("blogIds") List<Integer> blogIds);
}