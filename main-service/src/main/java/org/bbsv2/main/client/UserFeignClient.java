package org.bbsv2.main.client;

import org.bbsv2.common.Result;
import org.bbsv2.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "account-service", contextId = "userFeignClient", path = "/user")
public interface UserFeignClient {

  @GetMapping("/selectById/{id}")
  Result selectById(@PathVariable Integer id);

  // 解析返回的Result Obj -> User
  default User getUserById(Integer id) {
    Result result = selectById(id);
    if (result != null && "200".equals(result.getCode())) {
      return (User) result.getData();
    }
    return null;
  }

}
