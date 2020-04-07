package com.security.springsecuritystudy.config.mybatisplus;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 * mybatis-plus配置文件
 * 注册成bena的方式开启插件在多数据源情况下是无效的；需要手动加载至Configuration对象中
 * @author Jjcc
 * @version 1.0.0
 * @className MybatisPlusConfig.java
 * @createTime 2019年10月20日 17:46:00
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus的分页插件
     * @title paginationInterceptor
     * @author Jjcc
     * @return com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
     * @createTime 2019/10/20 17:48
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * SQL执行效率插件
     * 设置 dev test 环境开启
     * @title performanceInterceptor
     * @author Jjcc
     * @return com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor
     * @createTime 2019/10/20 17:49
     */
    @Bean
    @Profile({"test", "dev"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 逻辑删除用，3.1.1之后的版本可不需要配置该bean，但项目这里用的是3.1.1的
     * @title sqlInjector
     * @author Jjcc
     * @return com.baomidou.mybatisplus.core.injector.ISqlInjector
     * @createTime 2019/10/20 17:53
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

//    /**
//     * 加载配置文件中mybatis-plus的配置信息，配置多数据源所需要
//     * 这里获取bean的方式为多例，原因是多数据源集成mybatis-plus时，调用自带接口时，无法区分哪个数据源（MybatisConfiguration方法）；
//     * @title mybatisConfiguration
//     * @author Jjcc
//     * @param paginationInterceptor 分页插件
//     * @param performanceInterceptor SQL执行效率插件
//     * @return com.baomidou.mybatisplus.core.MybatisConfiguration
//     * @createTime 2019/10/22 9:23
//     */
//    @Scope("prototype")
//    @Bean("mybatisConfiguration")
//    @ConfigurationProperties(prefix = "mybatis-plus.configuration")
//    public MybatisConfiguration mybatisConfiguration(@Qualifier("paginationInterceptor") PaginationInterceptor paginationInterceptor,
//                                                      @Qualifier("performanceInterceptor") PerformanceInterceptor performanceInterceptor) {
//        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
//        mybatisConfiguration.addInterceptor(paginationInterceptor);
//        mybatisConfiguration.addInterceptor(performanceInterceptor);
//        return mybatisConfiguration;
//    }
//
//    @Scope("prototype")
//    @Bean("globalConfig")
//    @ConfigurationProperties(prefix = "mybatis-plus.global-config")
//    public GlobalConfig globalConfig() {
//        return new GlobalConfig();
//    }


}
