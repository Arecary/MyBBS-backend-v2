package org.bbsv2.main.service;

import org.bbsv2.common.entity.Account;
import org.bbsv2.main.entity.Collect;
import org.bbsv2.main.mapper.CollectMapper;
import org.bbsv2.main.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CollectService {

  @Resource
  CollectMapper collectMapper;

  public void set(Collect collect) {
    Account currentUser = TokenUtils.getCurrentUser();
    collect.setUserId(currentUser.getId());
    Collect dblCollect = collectMapper.selectUserCollect(collect);
    if (dblCollect == null) {
      collectMapper.insert(collect);
    } else {
      collectMapper.deleteById(dblCollect.getId());
    }
  }

  /**
   * 查询当前用户是否收藏过
   */
  public Collect selectUserCollect(Integer fid, String module) {
    Account currentUser = TokenUtils.getCurrentUser();
    Collect collect = new Collect();
    collect.setUserId(currentUser.getId());
    collect.setFid(fid);
    collect.setModule(module);
    return collectMapper.selectUserCollect(collect);
  }

  public int selectByFidAndModule(Integer fid, String module) {
    return collectMapper.selectByFidAndModule(fid, module);
  }

}
