package com.security.springsecuritystudy.config.exception;


/**
 * 异常类别的枚举，把异常分类固化下来。
 * @author Jjcc
 * @version 1.0.0
 * @className CustomExceptionType.java
 * @createTime 2019年10月17日 22:57:00
 */
public enum CustomExceptionType {

    /**
     * 异常类别固化初始化
     */
    USER_INPUT_ERROR(400, "用户输入异常"),
    REQUEST_TIME_OUT(408, "请求超时"),
    SYSTEM_ERROR(500, "系统异常"),
    AUTHORIZATION_ERROR(403, "权限不够"),
    CAPTCHACODE_ERROR(401, "验证码错误"),
    USER_NEED_AUTHORITIES(201,"用户未登录"),
    OTHER_ERROR(999, "其它异常");

    /**
     * 异常状态码
     */
    private int code;

    /**
     * 异常描述
     */
    private String typeDesc;

    CustomExceptionType(int code, String typeDesc) {
        this.code = code;
        this.typeDesc = typeDesc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
