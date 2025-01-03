package org.bbsv2.main.common.config;

import cn.hutool.core.util.ObjectUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.bbsv2.common.Constants;
import org.bbsv2.common.enums.ResultCodeEnum;
import org.bbsv2.common.enums.RoleEnum;
import org.bbsv2.common.exception.CustomException;
import org.bbsv2.common.entity.Account;

import org.bbsv2.main.client.AdminFeignClient;
import org.bbsv2.main.client.UserFeignClient;


import org.bbsv2.main.utils.TokenContext;
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
    private AdminFeignClient adminFeignClient; // OpenFeign

    @Resource
    private UserFeignClient userFeignClient;

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

        // 将 Token 保存到 ThreadLocal 中
        TokenContext.setToken(token);

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
                account = adminFeignClient.getAdminById(Integer.valueOf(userId));
            } else if (RoleEnum.USER.name().equals(role)) {
                account = userFeignClient.getUserById(Integer.valueOf(userId));
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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理 ThreadLocal 中的 Token
        TokenContext.clear();
        log.info("Token cleared from ThreadLocal after request completion.");
    }

}