package org.bbsv2.main.mapper;

import org.bbsv2.main.entity.Activity;

import java.util.List;

/**
 * 操作activity相关数据接口
 */
public interface ActivityMapper {

  /**
   * 新增
   */
  int insert(Activity activity);

  /**
   * 删除
   */
  int deleteById(Integer id);

  /**
   * 修改
   */
  int updateById(Activity activity);

  /**
   * 根据ID查询
   */
  Activity selectById(Integer id);

  /**
   * 查询所有
   */
  List<Activity> selectAll(Activity activity);

}
