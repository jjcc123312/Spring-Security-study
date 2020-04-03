package com.security.springsecuritystudy.config.beanconfig;

import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Druid中Spring监控
 * @author Jjcc
 * @version 1.0.0
 * @className DruidSpringInterceptor.java
 * @createTime 2019年10月11日 16:04:00
 */
@Configuration
public class DruidSpringInterceptor {


    /**
     * DruidStatInterceptor类为druid提供的拦截器
     * @title druidStatInterceptor
     * @author Jjcc
     * @return com.alibaba.druid.support.spring.stat.DruidStatInterceptor
     * @createTime 2019/10/11 15:37
     */
    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    /**
     * JdkRegexpMethodPointcut为使用正则表达式配置切点
     * @title druidStatPointcut
     * @author Jjcc
     * @return org.springframework.aop.support.JdkRegexpMethodPointcut
     * @createTime 2019/10/11 15:37
     */
    @Bean
    @Scope("prototype")
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPattern("com.jjcc.*");
        return pointcut;
    }

    /**
     * DefaultPointcutAdvisor类定义advice及 pointcut 属性。
     * advice指定使用的通知方式，也就是druid提供的DruidStatInterceptor类，pointcut指定切入点
     * @title druidStatAdvisor
     * @author Jjcc
     * @param druidStatInterceptor
     * @param druidStatPointcut
     * @return org.springframework.aop.support.DefaultPointcutAdvisor
     * @createTime 2019/10/11 15:38
     */
    @Bean
    public DefaultPointcutAdvisor druidStatAdvisor(DruidStatInterceptor druidStatInterceptor, JdkRegexpMethodPointcut druidStatPointcut) {
        DefaultPointcutAdvisor defaultPointAdvisor = new DefaultPointcutAdvisor();
        defaultPointAdvisor.setPointcut(druidStatPointcut);
        defaultPointAdvisor.setAdvice(druidStatInterceptor);
        return defaultPointAdvisor;
    }

}
