package org.bbsv2.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication(exclude = {
//        ContextInstanceDataAutoConfiguration.class,
//        ContextRegionProviderAutoConfiguration.class
//}, scanBasePackages = "org.bbsv2.main")

@EnableFeignClients
@SpringBootApplication(scanBasePackages = "org.bbsv2.main")
@MapperScan("org.bbsv2.main.mapper")
public class MainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

}
