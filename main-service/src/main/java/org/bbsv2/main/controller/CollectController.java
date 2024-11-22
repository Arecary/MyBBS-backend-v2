package org.bbsv2.main.controller;

import org.bbsv2.common.Result;
import org.bbsv2.main.entity.Collect;
import org.bbsv2.main.service.CollectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/collect")
public class CollectController {

  @Resource
  CollectService collectService;

  // 收藏和取消
  @PostMapping("/set")
  public Result set(@RequestBody Collect collect) {
    collectService.set(collect);
    return Result.success();
  }

}
