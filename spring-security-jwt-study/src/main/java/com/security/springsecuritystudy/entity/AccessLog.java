package com.security.springsecuritystudy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志实例
 * @title
 * @author Jjcc
 * @createTime 2019/10/22 16:49
 */
@Data
public class AccessLog implements Serializable {
    /**
     * 访问者用户名
     */
    private String username;
    /**
     * 请求路径
     */
    private String url;
    /**
     * 请求消耗时长
     */
    private Integer duration;
    /**
     * http 方法：GET、POST等
     */
    private String httpMethod;
    /**
     * http 请求响应状态码
     */
    private Integer httpStatus;
    /**
     * 访问者ip
     */
    private String ip;
    /**
     * 此条记录的创建时间
     */
    private String createTime;


}