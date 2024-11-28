package org.bbsv2.main.client;


import org.bbsv2.common.entity.Account;
import org.bbsv2.main.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "account-service", contextId = "tokenFeignClient", path = "/token", configuration = FeignConfig.class)
public interface TokenFeignClient {


  @GetMapping("/getCurrentUser")
  Account getCurrentUser(@RequestHeader("Authorization") String token);

}
