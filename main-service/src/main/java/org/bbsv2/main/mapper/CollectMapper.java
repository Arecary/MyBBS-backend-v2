package org.bbsv2.main.mapper;

import org.bbsv2.main.entity.Collect;
import org.apache.ibatis.annotations.Param;

public interface CollectMapper {

  void insert(Collect collect);

  Collect selectUserCollect(Collect collect);

  void deleteById(Integer id);

  int selectByFidAndModule(@Param("fid") Integer fid, @Param("module") String module);
}
