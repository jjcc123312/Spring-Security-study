package com.security.springsecuritystudy.config.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.springsecuritystudy.config.exception.AjaxResponse;
import com.security.springsecuritystudy.config.exception.CustomException;
import com.security.springsecuritystudy.config.exception.CustomExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 鉴权失败异常：认证用户访问无权限资源时的异常
 * @author Jjcc
 * @version 1.0.0
 * @className MyAccessDeniedHandlerImpl.java
 * @createTime 2020年04月01日 13:43:00
 */
@Component
public class MyAccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Value("${spring.security.loginType}")
    private String loginType;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        CustomException customException = new CustomException(CustomExceptionType.AUTHORIZATION_ERROR, "口令权限不足，无法访问");

        // 判断是需要跳转页面还是返回json。
        String type = "json";
        if (type.equalsIgnoreCase(loginType)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    objectMapper.writeValueAsString(AjaxResponse.error(customException)
                    ));
        } else {
            // 鉴权失败，跳转到指定的页面
            response.setContentType("text/html;charset=UTF-8");
           response.sendRedirect("/error");
        }


    }
}
