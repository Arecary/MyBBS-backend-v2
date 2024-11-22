package org.bbsv2.main.test;

import org.bbsv2.common.exception.CustomException;
import org.bbsv2.common.enums.ResultCodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  public String test() {
    throw new CustomException(ResultCodeEnum.SUCCESS);
  }
}
