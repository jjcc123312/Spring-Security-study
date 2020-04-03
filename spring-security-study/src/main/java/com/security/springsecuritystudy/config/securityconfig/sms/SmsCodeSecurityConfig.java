package com.security.springsecuritystudy.config.securityconfig.sms;

import com.security.springsecuritystudy.config.securityconfig.MyAuthenticationFailureHandler;
import com.security.springsecuritystudy.config.securityconfig.MyAuthenticationSuccessHandler;
import com.security.springsecuritystudy.config.securityconfig.MyUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 把所模拟于 用户密码认证授权Filter、用户密码认证授权 Provider 的 Sms 创建成一个配置对象。
 * 这里分开配置，然后在注入进总的 SecurityConfig
 * @author Jjcc
 * @version 1.0.0
 * @className SmsCodeSecurityConfig.java
 * @createTime 2020年03月30日 21:28:00
 */
@Component
public class SmsCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    private MyUserDetailsServiceImpl myUserDetailsService;

    private SmsCodeValidateFilter smsCodeValidateFilter;

    @Autowired
    public SmsCodeSecurityConfig(MyAuthenticationFailureHandler myAuthenticationFailureHandler,
                                 MyAuthenticationSuccessHandler myAuthenticationSuccessHandler,
                                 MyUserDetailsServiceImpl myUserDetailsService,
                                 SmsCodeValidateFilter smsCodeValidateFilter) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
        this.myUserDetailsService = myUserDetailsService;
        this.smsCodeValidateFilter = smsCodeValidateFilter;
    }

    @Override
    public void configure(HttpSecurity http) {
        // 认证授权过滤器对象
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        // 指定登录成功后的处理器
        smsAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        // 指定登录失败后的处理器
        smsAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        // 指定认证授权管理器；认证授权过滤器必须通过 authenticationManager 找到合适的 provider 进行认证授权
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        // 进行认证授权的对象
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        // set 进认证授权所需的 userDetails 对象
        smsCodeAuthenticationProvider.setUserDetailsService(myUserDetailsService);

        // 在 用户密码认证授权过滤器 的前面加入 短信验证码效验过滤器
        http.addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);

        // 在 用户密码认证授权过滤器 的后面加入 短信验证码认证授权过滤器
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}








