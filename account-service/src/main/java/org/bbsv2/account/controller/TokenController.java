package org.bbsv2.account.controller;

import org.bbsv2.account.utils.TokenUtils;
import org.bbsv2.common.entity.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * to get token
 */
@RestController
@RequestMapping("/token")
public class TokenController {


  @GetMapping("/getCurrentUser")
  public Account getCurrentUser(@RequestHeader("Authorization") String token) {
    System.out.println("Token received: " + token);
    Account account = TokenUtils.setUser(token);
    System.out.println("Account fetched: " + account);
    return account;
  }
}
