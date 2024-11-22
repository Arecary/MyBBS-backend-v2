package org.bbsv2.main.service;

import org.bbsv2.main.entity.Account;
import org.bbsv2.main.entity.Likes;
import org.bbsv2.main.mapper.LikesMapper;
import org.bbsv2.main.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LikesService {

  @Resource
  LikesMapper likesMapper;

  public void set(Likes likes) {
    Account currentUser = TokenUtils.getCurrentUser();
    likes.setUserId(currentUser.getId());
    Likes dblLikes = likesMapper.selectUserLikes(likes);
    if (dblLikes == null) {
      likesMapper.insert(likes);
    } else {
      likesMapper.deleteById(dblLikes.getId());
    }
  }

  /**
   * 查询当前用户是否点过赞
   */
  public Likes selectUserLikes(Integer fid, String module) {
    Account currentUser = TokenUtils.getCurrentUser();
    Likes likes = new Likes();
    likes.setUserId(currentUser.getId());
    likes.setFid(fid);
    likes.setModule(module);
    return likesMapper.selectUserLikes(likes);
  }

  public int selectByFidAndModule(Integer fid, String module) {
    return likesMapper.selectByFidAndModule(fid, module);
  }

}
