package org.bbsv2.account.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.bbsv2.common.Constants;
import org.bbsv2.common.enums.RoleEnum;
import org.bbsv2.common.entity.Account;
import org.bbsv2.account.service.AdminService;
import org.bbsv2.account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * Token tool class
 */
@Component
public class TokenUtils {

    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    private static AdminService staticAdminService;
    private static UserService staticUserService;

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    @PostConstruct
    public void setUserService() {
        staticAdminService = adminService;
        staticUserService = userService;
    }

    /**
     * 生成token
     */
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data) // 将 userId-role 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }

//    /**
//     * 获取当前登录的用户信息
//     */
//    public static Account getCurrentUser() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String token = request.getHeader(Constants.TOKEN);
//            log.info("Token received: {}", token); // 打印收到的 Token
//
//            if (ObjectUtil.isNotEmpty(token)) {
//                String userRole = JWT.decode(token).getAudience().get(0);
//                log.info("Decoded userRole: {}", userRole); // 打印解码后的 userRole
//
//                String userId = userRole.split("-")[0];  // 获取用户id
//                String role = userRole.split("-")[1];    // 获取角色
//                log.info("Decoded userId: {}, role: {}", userId, role); // 打印解析后的 userId 和 role
//
//                if (RoleEnum.ADMIN.name().equals(role)) {
//                    Account admin = staticAdminService.selectById(Integer.valueOf(userId));
//                    log.info("Fetched Admin account: {}", admin); // 打印查询到的 Admin 账号
//                    return admin;
//                } else if (RoleEnum.USER.name().equals(role)) {
//                    Account user = staticUserService.selectById(Integer.valueOf(userId));
//                    log.info("Fetched User account: {}", user); // 打印查询到的 User 账号
//                    return user;
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取当前用户信息出错", e);
//        }
//        System.out.println("Returning empty Account object");
//        return new Account();  // 返回空的账号对象
//    }


    /**
     * get user info by token
     */
    public static Account setUser(String token){
        try {
            log.info("Token received: {}", token); // 打印收到的 Token

            if (ObjectUtil.isNotEmpty(token)) {
                // 解码 JWT
                DecodedJWT jwt = JWT.decode(token);

                // 检查 Token 是否过期
                Date expiresAt = jwt.getExpiresAt();
                if (expiresAt.before(new Date())) {
                    log.error("Token has expired at: {}", expiresAt);
                    return new Account(); // 返回空对象，表示 Token 已过期
                }

                String userRole = JWT.decode(token).getAudience().get(0);
                log.info("Decoded userRole: {}", userRole); // 打印解码后的 userRole

                String userId = userRole.split("-")[0];  // 获取用户id
                String role = userRole.split("-")[1];    // 获取角色
                log.info("Decoded userId: {}, role: {}", userId, role); // 打印解析后的 userId 和 role

                if (RoleEnum.ADMIN.name().equals(role)) {
                    Account admin = staticAdminService.selectById(Integer.valueOf(userId));
                    log.info("Fetched Admin account: {}", admin); // 打印查询到的 Admin 账号
                    return admin;
                } else if (RoleEnum.USER.name().equals(role)) {
                    Account user = staticUserService.selectById(Integer.valueOf(userId));
                    log.info("Fetched User account: {}", user); // 打印查询到的 User 账号
                    return user;
                }
            }
        } catch (Exception e) {
            log.error("获取当前用户信息出错", e);
        }
        System.out.println("Returning empty Account object");
        return new Account();  // 返回空的账号对象
    }

}

