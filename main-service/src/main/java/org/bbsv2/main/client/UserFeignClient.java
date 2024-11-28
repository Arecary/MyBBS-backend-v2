package org.bbsv2.main.client;

import com.fasterxml.jackson.databind.ObjectMapper;

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
      Object data = result.getData();
      // 使用 ObjectMapper 将 LinkedHashMap 转换为 User，因为return (User) result.getData();会导致强转异常ClassCastException
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.convertValue(data, User.class);
    }
    return null;
  }

}
