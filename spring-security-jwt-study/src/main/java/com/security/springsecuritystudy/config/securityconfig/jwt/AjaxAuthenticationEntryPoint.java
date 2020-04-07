package com.security.springsecuritystudy.config.securityconfig.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.springsecuritystudy.config.exception.AjaxResponse;
import com.security.springsecuritystudy.config.exception.CustomException;
import com.security.springsecuritystudy.config.exception.CustomExceptionType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求未认证 的处理
 * @author Jjcc
 */
@Component
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.setContentType("application/json;charset=UTF-8");

        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(
                AjaxResponse.error(new CustomException(CustomExceptionType.USER_NEED_AUTHORITIES, "未登录"))));

        // 未登录重定向到登录页面
//        httpServletResponse.sendRedirect("/login.html");
    }
}