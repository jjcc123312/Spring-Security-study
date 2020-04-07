package com.security.springsecuritystudy.config.securityconfig.jwt;

import com.security.springsecuritystudy.config.exception.AjaxResponse;
import com.security.springsecuritystudy.config.exception.CustomException;
import com.security.springsecuritystudy.config.exception.CustomExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className JwtAuthController.java
 * @createTime 2020年04月03日 23:37:00
 */
@RestController
public class JwtAuthController {

    private JwtAuthService jwtAuthService;



    @Autowired
    public JwtAuthController(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    /**
     * 登录认证
     * @title login
     * @author Jjcc
     * @param username 参数容器
     * @param password 参数容器
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2020/4/4 13:53
     */
    @PostMapping("/login")
    public AjaxResponse login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return AjaxResponse.error(
                    new CustomException(
                            CustomExceptionType.USER_INPUT_ERROR,"用户名密码不能为空"));
        }

        HashMap<String, String> map = new HashMap<>(2);
        String token = jwtAuthService.login(username, password);
        map.put("headerName", jwtAuthService.getHedaerName());
        map.put("token", token);
        map.put("url", "/index");

        return AjaxResponse.loginSuccess(map);
    }

    /**
     * 刷新令牌
     * @title refresh
     * @author Jjcc
     * @param token token
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2020/4/4 13:53
     */
    @PostMapping(value = "/refreshToken")
    public AjaxResponse refresh(@RequestHeader("${jwt.header}") String token) {
        return AjaxResponse.success(jwtAuthService.refreshToken(token));
    }
}
