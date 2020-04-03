package com.security.springsecuritystudy.config.securityconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出成功的处理器
 * @author Jjcc
 * @version 1.0.0
 * @className MyLogoutSuccessHandler.java
 * @createTime 2020年03月28日 17:39:00
 */
@Component
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("退出成功");

        response.sendRedirect("/login.html");
    }
}
