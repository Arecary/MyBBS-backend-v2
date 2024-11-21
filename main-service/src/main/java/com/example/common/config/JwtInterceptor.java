package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import common.enums.ResultCodeEnum;
import common.enums.RoleEnum;
import common.exception.CustomException;

import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 打印请求路径
        log.info("Intercepting request: {}", request.getRequestURI());

        // 1. 从 http 请求的 header 中获取 token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            // 如果没拿到，从参数里再拿一次
            token = request.getParameter(Constants.TOKEN);
        }

        // 打印获取到的 Token
        log.info("Token received: {}", token);

        // 2. 开始执行认证
        if (ObjectUtil.isEmpty(token)) {
            log.error("Token is missing. Rejecting request.");
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }

        Account account = null;
        try {
            // 解析 token 获取存储的数据
            String userRole = JWT.decode(token).getAudience().get(0);
            String userId = userRole.split("-")[0];
            String role = userRole.split("-")[1];

            // 打印解析出的 userId 和 role
            log.info("Decoded token - userId: {}, role: {}", userId, role);

            // 根据 userId 查询数据库
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            } else if (RoleEnum.USER.name().equals(role)) {
                account = userService.selectById(Integer.valueOf(userId));
            }

            // 打印查询结果
            log.info("Account found: {}", account);
        } catch (Exception e) {
            log.error("Error decoding or fetching account: {}", e.getMessage(), e);
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        if (ObjectUtil.isNull(account)) {
            log.error("Account not found for the given token.");
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }

        try {
            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token); // 验证 token

            // 打印验证成功日志
            log.info("Token successfully verified for userId: {}", account.getId());
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed: {}", e.getMessage());
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        return true;
    }


/*    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            token = request.getParameter(Constants.TOKEN);
        }

        if (ObjectUtil.isEmpty(token)) {
            log.error("Token is missing in the request.");
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }

        Account account = null;
        try {
            String userRole = JWT.decode(token).getAudience().get(0);
            String userId = userRole.split("-")[0];
            String role = userRole.split("-")[1];

            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            } else if (RoleEnum.USER.name().equals(role)) {
                account = userService.selectById(Integer.valueOf(userId));
            }

            if (ObjectUtil.isNull(account)) {
                log.error("Account not found for userId: {}", userId);
                throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
            }

            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed: {}", e.getMessage());
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error during token validation: {}", e.getMessage(), e);
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        return true;
    }*/

}