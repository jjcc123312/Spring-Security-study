package com.security.springsecuritystudy.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className CustomAsyncRequestTimeoutException.java
 * @createTime 2019年10月24日 09:02:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CustomAsyncRequestTimeoutException extends RuntimeException {

    /**
     * 异常错误编码
     */
    private int code;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * 异常信息
     */
    private String message;

}
