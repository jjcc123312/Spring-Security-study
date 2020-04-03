package com.security.springsecuritystudy.config.securityconfig.sms;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 模拟账号密码登录，这里模拟 DaoAuthenticationProvider 实现
 * @author Jjcc
 * @version 1.0.0
 * @className SmsCodeAuthenticationProvider.java
 * @createTime 2020年03月30日 14:57:00
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    /**
     * 数据库获取权限的对象
     */
    private UserDetailsService userDetailsService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;

        // 拿到 authenticationToken 中的手机号码
        String mobile = (String) smsCodeAuthenticationToken.getPrincipal();

        // 通过手机号码拿到相对应的权限
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        if(userDetails == null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(smsCodeAuthenticationToken.getDetails());


        return authenticationResult;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 类型或其子类或其子接口
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}




