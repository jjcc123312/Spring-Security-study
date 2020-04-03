package com.security.springsecuritystudy.config.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.springsecuritystudy.config.exception.AjaxResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 登录成功的处理
 * @author Jjcc
 * @version 1.0.0
 * @className MyAuthenticationSuccessHandler.java
 * @createTime 2020年03月22日 13:26:00
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${spring.security.loginType}")
    private String loginType;

    private  static ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        String type = "json";
        // 判断是需要跳转页面还是返回json。
        if (type.equalsIgnoreCase(loginType)) {

            // 上一次请求的页面，会存储在 session key 为 SPRING_SECURITY_SAVED_REQUEST的session中。
            // 当为null时，则表明他没有请求其他页面，直接进入的登录页面
            SavedRequest lasRequest = (SavedRequest) request.getSession(false).getAttribute("SPRING_SECURITY_SAVED_REQUEST");

            Optional<SavedRequest> lasRequestOpt = Optional.ofNullable(lasRequest);

            String url;
            if (lasRequestOpt.isPresent()) {
                url = lasRequestOpt.get().getRedirectUrl();
            } else {
                url = "/index";
            }

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.success(url)));
        } else {
            // 调用父类方法；指的是登录成功后，会跳转到上一次请求的页面中。
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}



