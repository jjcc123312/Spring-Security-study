package com.security.springsecuritystudy.config.securityconfig;

import com.google.code.kaptcha.Constants;
import com.security.springsecuritystudy.entity.CaptchaImageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
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
 * 验证验证码 filter
 * @author Jjcc
 * @version 1.0.0
 * @className CaptchaCodeFilter.java
 * @createTime 2020年03月29日 16:43:00
 */
@Component
public class CaptchaCodeFilter extends OncePerRequestFilter {

    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    public CaptchaCodeFilter(MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = "/login";
        String method = "post";

        // 必须是登录的post请求才能进行验证，其他的直接放行
        if (StringUtils.equals(url, request.getRequestURI())
                && StringUtils.equalsIgnoreCase(method, request.getMethod())) {
            try {
                // 1. 进行验证码的校验
                validate(new ServletWebRequest(request));
            } catch (AuthenticationException e) {

                // 2. 捕获步骤1中校验出现异常，交给失败处理类进行进行处理；注意，后需要 return
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e );
                return;
            }
        }

        // 进入下一个 filter
        filterChain.doFilter(request, response);
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        HttpSession httpSession = request.getRequest().getSession();

        // 1.获取请求中的验证码
        String captchaCode = ServletRequestUtils.getStringParameter(request.getRequest(), "captchaCode");

        // 2.验证空值
        if (StringUtils.isEmpty(captchaCode)) {
            throw new SessionAuthenticationException("验证码不能为空");
        }

        // 3.获取服务器session池中的验证码
        CaptchaImageVO codeSession = (CaptchaImageVO) httpSession.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (Objects.isNull(codeSession)) {
            throw new SessionAuthenticationException("验证码不存在");
        }

        // 4.校验服务器session池中的验证码是否过期
        if (codeSession.isExpried()) {
            httpSession.removeAttribute("Constants.KAPTCHA_SESSION_KEY");
            throw new SessionAuthenticationException("验证码已经过期");
        }

        // 5. 验证码校验

        if (!StringUtils.equalsIgnoreCase(captchaCode, codeSession.getCode())) {
            throw new SessionAuthenticationException("验证码不匹配");
        }

        // 至此，验证码效验完成，移除验证码
        httpSession.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
    }

}
