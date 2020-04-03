package com.security.springsecuritystudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 项目启动类
 * @title
 * @author Jjcc 
 * @return 
 * @createTime 2019/11/18 21:19
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
@ServletComponentScan
@EnableAsync
@EnableRedisRepositories
@EnableCaching
@MapperScan(basePackages = "com.security.springsecuritystudy.dao.mysqldao*")
public class SpringSecurityStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityStudyApplication.class, args);
    }

}
