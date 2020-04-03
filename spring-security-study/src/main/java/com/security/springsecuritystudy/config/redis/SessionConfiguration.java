package com.security.springsecuritystudy.config.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 自动化配置 Spring Session 使用 Redis 作为数据源
 * @author Jjcc
 * @version 1.0.0
 * @className SessionConfiguration.java
 * @createTime 2020年04月01日 17:24:00
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfiguration {


    /**
     * 创建 {@link RedisOperationsSessionRepository} 使用的 RedisSerializer Bean 。
     *
     * 具体可以看看 {@link RedisHttpSessionConfiguration#setDefaultRedisSerializer(RedisSerializer)} 方法，
     * 它会引入名字为 "springSessionDefaultRedisSerializer" 的 Bean 。
     *
     * @return RedisSerializer Bean
     */
//    @Bean(name = "springSessionDefaultRedisSerializer")
//    @Qualifier("redisTemplate")
//    public RedisSerializer springSessionDefaultRedisSerializer(RedisTemplate redisTemplate) {
//        return redisTemplate.getValueSerializer();
//    }



}
