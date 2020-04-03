package com.security.springsecuritystudy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className SmsCode.java
 * @createTime 2020年03月30日 15:52:00
 */
@Data
public class SmsCode implements Serializable {

    /**
     * 短信验证码
     */
    private String code;
    /**
     * 验证码的过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 发送手机号
     */
    private String mobile;

    public SmsCode(String code,int expireAfterSeconds,String mobile){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
        this.mobile = mobile;
    }

    public SmsCode() {

    }

    /**
     * @JsonIgnore：指示基于内省的序列化和反序列化功能将忽略访问器
     * 不使用该注解，会出现 Jackson2JsonRedisSerializer报错Could not read JSON: Unrecognized field...
     * @title isExpired
     * @author Jjcc
     * @return boolean
     * @createTime 2020/4/2 15:35
     */
    @JsonIgnore
    public boolean isExpired(){
        return  LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public String getMobile() {
        return mobile;
    }
}
