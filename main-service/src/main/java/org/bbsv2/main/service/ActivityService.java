package org.bbsv2.main.service;

import org.bbsv2.main.entity.Activity;
import org.bbsv2.main.mapper.ActivityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动业务处理
 **/
@Service
public class ActivityService {

  @Resource
  private ActivityMapper activityMapper;

  /**
   * 新增
   */
  public void add(Activity activity) {
    activityMapper.insert(activity);
  }

  /**
   * 删除
   */
  public void deleteById(Integer id) {
    activityMapper.deleteById(id);
  }

  /**
   * 批量删除
   */
  public void deleteBatch(List<Integer> ids) {
    for (Integer id : ids) {
      activityMapper.deleteById(id);
    }
  }

  /**
   * 修改
   */
  public void updateById(Activity activity) {
    activityMapper.updateById(activity);
  }

  /**
   * 根据ID查询
   */
  public Activity selectById(Integer id) {
    return activityMapper.selectById(id);
  }

  /**
   * 查询所有
   */
  public List<Activity> selectAll(Activity activity) {
    return  activityMapper.selectAll(activity);
  }

  /**
   * 分页查询
   */
  public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    List<Activity> list = activityMapper.selectAll(activity);
    return PageInfo.of(list);
  }

  /**
   * 热门活动
   */
  public List<Activity> selectTop() {
    List<Activity> activityList = this.selectAll(null);
    activityList = activityList.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
            .limit(2)
            .collect(Collectors.toList());
    return activityList;
  }
}
