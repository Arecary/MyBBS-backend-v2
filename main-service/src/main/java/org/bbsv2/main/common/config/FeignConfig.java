package org.bbsv2.main.common.config;

import org.bbsv2.main.utils.TokenContext;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig implements RequestInterceptor {


  private static final Logger log = LoggerFactory.getLogger(FeignConfig.class);

  @Override
  public void apply(RequestTemplate template) {
    String token = TokenContext.getToken();
    System.out.println("token in FeignConfig is: " + token);
    if (token != null) {
      template.header("Authorization", token);
      log.info("Adding Authorization header to Feign request: {}", token);
    } else {
      log.warn("Token is null, Authorization header will not be added");
    }
  }

}
