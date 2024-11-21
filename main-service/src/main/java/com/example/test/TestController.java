package com.example.test;

import common.exception.CustomException;
import common.enums.ResultCodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  public String test() {
    throw new CustomException(ResultCodeEnum.SUCCESS);
  }
}
