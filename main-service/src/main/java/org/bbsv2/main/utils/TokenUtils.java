package org.bbsv2.main.utils;

import org.bbsv2.common.entity.Account;
import org.bbsv2.main.client.TokenFeignClient;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/*
 * Token tool class
 */


@Component
public class TokenUtils {

  private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);
  private static TokenFeignClient staticTokenFeignClient;

  @Resource
  private TokenFeignClient tokenFeignClient;

  @PostConstruct
  public void init() {
    staticTokenFeignClient = tokenFeignClient;
  }
  /*
   * to get current user
   */
  public static Account getCurrentUser() {
    try {
//      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//      System.out.println("HttpServletRequest: " + request);
//      String token = request.getHeader("Authorization");
//      System.out.println("Token from request: " + token);
      // 从 TokenContext 中获取 Token
      String token = TokenContext.getToken();
      System.out.println("Token is: " + token);


      if (token != null && !token.isEmpty()) {
        System.out.println("Feign Client is about to call account-service...");
        Account account = staticTokenFeignClient.getCurrentUser(token);
        System.out.println("Account from Feign Client: " + account);
        return account;
      }
    } catch (Exception e) {
      log.error("Failed to fetch current user", e);
    }
    return new Account();  // 返回空的账号对象
  }
}

