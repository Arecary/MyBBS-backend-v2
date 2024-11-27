package org.bbsv2.main.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.bbsv2.common.Result;
import org.bbsv2.common.entity.Admin;


@FeignClient(name = "account-service", contextId = "adminFeignClient", path = "/admin")
public interface AdminFeignClient {

  @GetMapping("/selectById/{id}")
  Result selectById(@PathVariable Integer id);

  // 解析返回的Result Obj -> Admin
  default Admin getAdminById(Integer id) {
    Result result = selectById(id);
    if (result != null && "200".equals(result.getCode())) {
      return (Admin) result.getData();
    }
    return null;
  }

}
