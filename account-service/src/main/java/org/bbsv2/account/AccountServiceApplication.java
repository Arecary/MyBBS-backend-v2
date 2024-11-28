package org.bbsv2.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


//@SpringBootApplication(exclude = {
//        ContextStackAutoConfiguration.class,
//        ContextInstanceDataAutoConfiguration.class,
//        ContextRegionProviderAutoConfiguration.class
//
//}, scanBasePackages = "org.bbsv2.account")


@SpringBootApplication(scanBasePackages = {"org.bbsv2.account", "org.bbsv2.common"})
@MapperScan("org.bbsv2.account.mapper")
@EntityScan(basePackages = "org.bbsv2.common.entity")
public class AccountServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(AccountServiceApplication.class, args);
  }
}
