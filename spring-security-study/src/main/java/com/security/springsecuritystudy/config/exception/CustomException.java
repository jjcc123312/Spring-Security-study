package com.security.springsecuritystudy.config.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 * @author Jjcc
 * @version 1.0.0
 * @className AjaxResponse.java
 * @createTime 2019年10月17日 23:04:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException{

    /**
     * 异常错误编码
     */
    private int code;
    /**
     * 异常信息
     */
    private String message;

    public CustomException(CustomExceptionType customExceptionType, String message) {
        this.code = customExceptionType.getCode();
        this.message = message;
    }
}
