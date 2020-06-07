package com.security.springsecuritystudy.config.securityconfig;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.springsecuritystudy.config.securityconfig.jwt.AjaxAuthenticationEntryPoint;
import com.security.springsecuritystudy.config.securityconfig.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;

/**
 * EnableGlobalMethodSecurity=true ：开启对 Spring Security 注解的方法，进行权限验证
 * @author Jjcc
 * @version 1.0.0
 * @className SecurityConfig.java
 * @createTime 2020年03月21日 17:08:00
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 登录成功处理器
     */
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    /**
     * 登录失败处理器
     */
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    /**
     * 完成认证与授权的对象
     */
    private MyUserDetailsServiceImpl myUserDetailsService;

    /**
     * 退出成功处理器
     */
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    /**
     * 数据源对象；rememberMe功能时，通过数据库表单验证所需
     */
    private DataSource dataSource;

    /**
     * 验证码效验过滤器
     */
    private CaptchaCodeFilter captchaCodeFilter;

    /**
     * 鉴权失败处理器
     */
    private MyAccessDeniedHandlerImpl myAccessDeniedHandler;

    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig (MyAuthenticationSuccessHandler myAuthenticationSuccessHandler,
                           MyAuthenticationFailureHandler myAuthenticationFailureHandler,
                           DataSource dataSource,
                           MyUserDetailsServiceImpl myUserDetailsService,
                           MyLogoutSuccessHandler myLogoutSuccessHandler,
                           MyAccessDeniedHandlerImpl myAccessDeniedHandler,
                           JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                           AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint,
                           CaptchaCodeFilter captchaCodeFilter) {
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
        this.myUserDetailsService = myUserDetailsService;
        this.dataSource = dataSource;
        this.myLogoutSuccessHandler = myLogoutSuccessHandler;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.ajaxAuthenticationEntryPoint = ajaxAuthenticationEntryPoint;
        this.captchaCodeFilter = captchaCodeFilter;
    }

    /**
     * 配置URL的权限等
     * @param http URL配置对象
     * @return void
     * @title configure
     * @author Jjcc
     * @createTime 2020/3/21 22:13
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用form表单登录
        http.formLogin().disable();

        // <X> 配置请求地址的权限
        http.authorizeRequests()
        // 不需要权限即可访问的 URL；#permitAll()：允许访问的URL
        .antMatchers("/login.html", "/login", "invalidSession.html", "/kaptcha"
                , "/smsCode/*", "/sms/login", "/refreshToken", "/redisCacheCode", "/hello").permitAll()
        // 首页无需权限验证，只要登录即可
        .antMatchers("/index").authenticated()
        // 自定义验证，根据url效验
        .anyRequest().access("@rabcService.hasPermission(request, authentication)");

        // 鉴权失败的处理器
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

        // 请求未认证时的处理器
        http.exceptionHandling().authenticationEntryPoint(ajaxAuthenticationEntryPoint);

        // <Z> 退出的URL
        http.logout()
                // 退出URL
                .logoutUrl("/signout")
//                .logoutSuccessUrl("/login.html")
                // 退出后删除 cookie
                .deleteCookies("JSESSIONID")
                // 退出成功后执行的处理器
                .logoutSuccessHandler(myLogoutSuccessHandler);

        // 禁用跨站csrf攻击防御
        http.csrf().disable();


        // migrateSession：每次登录验证将创建一个新的HTTP会话，旧的HTTP会话将无效，并且旧会话的属性将被复制。
        // 这样避免了在浏览器使用 sessionID 绕过登录。
        // none：原始会话不会失效。newSession：将创建一个干净的会话，而不会复制旧会话中的任何属性
        http.sessionManagement().sessionFixation().migrateSession();

        // maximumSessions()：同一个用户最大的登录数量
        // maxSessionsPreventsLogin()：true：表示已登录就不再允许再次登录；false：表示允许再次登录，但之前的登录会下线
        // expiredSessionStrategy()：自定义session被下线后或者超时的处理策略
        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(new CustomExpiredSessionStrategy());

        // 添加自定义的验证码效验 filter，在 UsernamePasswordAuthenticationFilter 之前执行
        http.addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class);

        // spring security 禁止使用 session，使用 jwt令牌时，无需session配合使用了
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 添加自定义的 jwt令牌鉴权过滤器，在 UsernamePasswordAuthenticationFilter 之前执行
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 将自定义的 短信验证码配置添加进 spring Security中
//        http.apply(smsCodeSecurityConfig);
    }

    /**
     * 配置用户及对应的权限。
     * @title configure
     * @author Jjcc
     * @param auth 认证管理对象
     * @createTime 2020/3/21 22:24
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用用户自定义的 userDetailsService，从数据库中拿取相关信息。
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * BCrypt加密
     * @title passwordEncoder
     * @author Jjcc
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @createTime 2020/3/21 22:32
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 放行静态资源
     * @title configure
     * @author Jjcc
     * @param web 对象
     * @return void
     * @createTime 2020/3/27 18:30
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/image/**", "/js/**", "/css/**", "/*.ico");
    }

    /**
     * 自定义session过期或者被下线后的执行策略
     * @title
     * @author Jjcc
     * @createTime 2020/3/24 11:53
     */
    public static class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {

        //jackson的JSON处理对象
        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
            HashMap<String, Object> map = new HashMap<>(4);

            map.put("code", 0);
            map.put("msg", "您的登录已经超时或者已经在另一台机器登录，您被迫下线。" +
                    event.getSessionInformation().getLastRequest());

            String json = objectMapper.writeValueAsString(map);
            HttpServletResponse response = event.getResponse();

            // 输出json信息的数据。
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);

            // 或者是页面跳转
//            response.sendRedirect("/login.html");
        }
    }



    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    /**
     * 自定义登录需要使用 authenticationToken，所以将声明一个 authenticationManager Bean
     * @title authenticationManagerBean
     * @author Jjcc
     * @return org.springframework.security.authentication.AuthenticationManager
     * @createTime 2020/4/4 13:56
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}





























