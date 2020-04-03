package com.security.springsecuritystudy.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Objects;

/**
 * 配置Redis序列化方式
 * @AutoConfigureAfter：在指定的类实例化后开始加载本类
 * @author Jjcc
 * @version 1.0.0
 * @className RedisConfiguration.java
 * @createTime 2019年11月10日 16:13:00
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfiguration {

    /**
     * 设置redis存储数据的序列化方式
     * @title redisTemplate
     * @author Jjcc
     * @param redisConnectionFactory redis连接工厂
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.lang.Object>
     * @createTime 2019/11/12 20:48
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //设置 RedisConnection 工厂。它就是实现多种 Java Redis 客户端接入的秘密工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 创建 Jackson2JsonRedisSerializer JACKSON2序列化策略对象
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        //允许更改底层VisibilityCheckers的配置，以更改自动检测的属性类型的详细信息
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 反序列化时出现 Could not read JSON: Unrecognized field "enabled" 问题的解决方法
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Redis序列化 Java8的时间Instant、LocalDateTime、LocalDate所需配置
        objectMapper.registerModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule());

        //设置JACKSON2序列化策略对象
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //使用 String 序列化方式，序列化 KEY
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //使用 Jackson2 序列化方式，序列化 VALUE
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //使用 String 序列化方式，序列化 HASHKEY
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //使用 Jackson2 序列化方式，序列化 HASHVALUE
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }

    /**
     * 设置CacheManager缓存管理类
     * @title redisCacheManager
     * @author Jjcc
     * @param redisTemplate redis模板对象
     * @return org.springframework.data.redis.cache.RedisCacheManager
     * @createTime 2019/11/12 20:48
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));

        //缓存配置对象
         RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        //设置缓存的默认超时时间：30分钟
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L)).
                //如果是空值，不缓存
                disableCachingNullValues().
                //设置key序列化器
                serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())).
                //设置value序列化器
                serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));

        return RedisCacheManager
                .builder(redisCacheWriter)
                .cacheDefaults(redisCacheConfiguration).build();
    }
}





