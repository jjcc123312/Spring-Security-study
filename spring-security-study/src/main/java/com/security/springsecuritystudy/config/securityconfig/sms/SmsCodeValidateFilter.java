package com.security.springsecuritystudy.config.securityconfig.sms;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.springsecuritystudy.config.securityconfig.MyAuthenticationFailureHandler;
import com.security.springsecuritystudy.entity.SmsCode;
import com.security.springsecuritystudy.entity.User;
import com.security.springsecuritystudy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 短信验证码效验过滤器
 * @author Jjcc
 * @version 1.0.0
 * @className SmsCodeValidateFilter.java
 * @createTime 2020年03月30日 16:35:00
 */
@Component
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    private UserService userService;

    @Autowired
    public SmsCodeValidateFilter(MyAuthenticationFailureHandler myAuthenticationFailureHandler,
                                 UserService userService) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = "/sms/login";
        String method = "post";

        // 必须是短信登录的post请求才能进行验证，其他的直接放行
        if (StringUtils.equalsIgnoreCase(url, request.getRequestURI())
                && StringUtils.equalsIgnoreCase(method, method)) {
            try {
                // 短信验证码效验
                validate(new ServletWebRequest(request));
            } catch (AuthenticationException e) {
                // 捕获异常交给自定义的失败处理器来处理；注意，后需要 return
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request,response);
    }

    private void validate(ServletWebRequest request) {
        // 手机号码
        String mobile = request.getParameter("mobile");
        // 输入的短信验证码
        String inputCode = request.getParameter("smsCode");

        HttpSession session = request.getRequest().getSession();
        // 从session中拿到前面 请求验证码 阶段生成的 验证码对象
        SmsCode smsSession = (SmsCode) session.getAttribute("sms_key");

        if (StringUtils.isEmpty(mobile)) {
            throw new SessionAuthenticationException("手机号码不能为空");
        }

        if (StringUtils.isEmpty(inputCode)) {
            throw new SessionAuthenticationException("短信验证码不能为空");
        }

        if (Objects.isNull(smsSession)) {
            throw new SessionAuthenticationException("短信验证码不存在");
        }

        if (smsSession.isExpired()) {
            throw new SessionAuthenticationException("短信验证码已过期");
        }

        if (!inputCode.equalsIgnoreCase(smsSession.getCode())) {
            throw new SessionAuthenticationException("短信验证码不正确");
        }

        if (!mobile.equalsIgnoreCase(smsSession.getMobile())) {
            throw new SessionAuthenticationException("短信发送目标与该手机号不一致");
        }

        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("mobile", mobile);

        User one = userService.getOne(objectQueryWrapper);

        if (Objects.isNull(one)) {
            throw new SessionAuthenticationException("您输入的手机号不是系统的注册用户");
        }

        session.removeAttribute("sms_key");
    }
}














