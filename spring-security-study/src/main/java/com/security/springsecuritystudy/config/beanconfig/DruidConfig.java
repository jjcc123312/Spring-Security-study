package com.security.springsecuritystudy.config.beanconfig;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * druid的sql监视功能
 * @author Jjcc
 * @version 1.0.0
 * @className DruidConfig.java
 * @createTime 2019年10月11日 15:23:00
 */
@Configuration
public class DruidConfig {

    /**
     * 配置Druid监控
     * 后台管理Servlet
     * @title druidServlet
     * @author Jjcc 
     * @return org.springframework.boot.web.servlet.ServletRegistrationBean
     * @createTime 2019/10/11 15:33
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        //白名单
        Map<String,String> initParameters = new HashMap<>(16);
        //禁用HTML页面上的“REST ALL”功能
        initParameters.put("resetEnable","false");
        //IP白名单（没有配置或者为空，则允许所有访问）
        initParameters.put("/druid/*","");
        //监控页面登录用户名
        initParameters.put("loginUsername","admin");
        //监控页面登录用户密码
        initParameters.put("loginPassword", "admin");
        //ip黑名单
        initParameters.put("deny","");
        reg.setInitParameters(initParameters);
        return reg;
    }


    /**
     * 配置web监控的filter
     * @title filterRegistrationBean
     * @author Jjcc
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @createTime 2019/10/11 15:33
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName","USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName","USER_SESSION");
        filterRegistrationBean.addInitParameter("DruidWebStatFilter","/*");
        return filterRegistrationBean;
    }


}
