package com.security.springsecuritystudy.config.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.springsecuritystudy.config.exception.AjaxResponse;
import com.security.springsecuritystudy.config.exception.CustomException;
import com.security.springsecuritystudy.config.exception.CustomExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录失败的处理
 * @author Jjcc
 * @version 1.0.0
 * @className MyAuthenticationFailureHandler.java
 * @createTime 2020年03月22日 13:38:00
 */
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${spring.security.loginType}")
    private String loginType;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Map<Class<? extends AuthenticationException>, CustomException> map = new HashMap<>();

    /**
     *  未知异常
     */
    private static CustomException defaultException = new CustomException(CustomExceptionType.OTHER_ERROR, CustomExceptionType.OTHER_ERROR.getTypeDesc());

    static {
        map.put(UsernameNotFoundException.class, new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在"));
        map.put(DisabledException.class, new CustomException(CustomExceptionType.AUTHORIZATION_ERROR, "用户已被禁止"));
        map.put(BadCredentialsException.class, new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名或密码存在错误，请检查后再次登录"));
        map.put(LockedException.class, new CustomException(CustomExceptionType.AUTHORIZATION_ERROR, "账号锁定"));
        map.put(AccountExpiredException.class, new CustomException(CustomExceptionType.AUTHORIZATION_ERROR, "账户过期"));
        map.put(CredentialsExpiredException.class, new CustomException(CustomExceptionType.AUTHORIZATION_ERROR, "证书过期"));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        Class<? extends AuthenticationException> exceptionClass = exception.getClass();

        CustomException orDefault = map.getOrDefault(exceptionClass, defaultException);

        // 这里异常类型如果是 SessionAuthenticationException，表示是验证码效验异常
        // message 取异常中自带的
        if (exception instanceof SessionAuthenticationException) {
            orDefault = new CustomException(CustomExceptionType.CAPTCHACODE_ERROR, exception.getMessage());
        }

        // 判断是需要跳转页面还是返回json。
        String type = "json";
        if (type.equalsIgnoreCase(loginType)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    objectMapper.writeValueAsString(AjaxResponse.error(orDefault)
            ));
        } else {
            // 登录失败，跳转到默认的failureUrl
            response.setContentType("text/html;charset=UTF-8");
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}




