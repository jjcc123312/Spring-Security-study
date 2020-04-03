package com.security.springsecuritystudy.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className CaptchaImageVO.java
 * @createTime 2020年03月29日 16:06:00
 */
@Data
public class CaptchaImageVO implements Serializable {

    /**
     * 验证码文字
     */
    private String code;

    /**
     * 验证码失效时间
     */
    private LocalDateTime expireTime;

    public CaptchaImageVO(String code, int expireAfterSeconds){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
    }

    /**
     * 验证码是否失效
     * @title isExpried
     * @author Jjcc
     * @return boolean
     * @createTime 2020/3/29 16:06
     */
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
