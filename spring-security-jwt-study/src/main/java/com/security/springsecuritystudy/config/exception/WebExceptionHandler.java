package com.security.springsecuritystudy.config.exception;

import com.security.springsecuritystudy.config.securityconfig.MyAuthenticationFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 全局处理异常类
 * @author Jjcc
 * @version 1.0.0
 * @className WebExceptionHandler.java
 * @createTime 2019年10月19日 15:35:00
 */
@Slf4j
@ControllerAdvice
public class WebExceptionHandler {


    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    public WebExceptionHandler(MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }

    /**
     * 方法抛出customException异常后返回的数据结构
     * @title customException
     * @author Jjcc
     * @param e
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/19 15:38
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public AjaxResponse customException(CustomException e) {
        if (e.getCode() == CustomExceptionType.SYSTEM_ERROR.getCode()) {
            //系统异常可以做持久化处理，方便运维人员处理
            log.error("自定义异常-------->", e);
        }

        return AjaxResponse.error(e);
    }

    /**
     * 在JwtService中，登录实际是交给 authenticationManager来选择对应的 provider来处理。
     * 处理过程中，可能出现的各种异常都是 AuthenticationException的子类，这里我将抛出的异常交给
     * formLog 方式登录所需指定的 认证失败处理器 AuthenticationFailureHandler 进行处理
     * @title customAuthenticationException
     * @author Jjcc
     * @param request 请求体
     * @param response 响应体
     * @param e
     * @return void
     * @createTime 2020/4/5 23:46
     */
    @ExceptionHandler(AuthenticationException.class)
    public void customAuthenticationException(HttpServletRequest request, HttpServletResponse response,
                                                      AuthenticationException e) throws IOException, ServletException {
        log.error("exception--------11>", e);
        myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AjaxResponse exception(Exception e) {
        //针对位置异常可以做持久化处理，方便运维人员处理
        log.error("exception-------->", e);
        return AjaxResponse.error(new CustomException(CustomExceptionType.OTHER_ERROR, "未知异常"));
    }

    /**
     * 该监听 无法处理 Spring Security抛出的异常；
     * 也不是所有的异常，目前笔者发现只有 @PreAuthorize() 注解在方法层面验证权限时可以捕获Spring Security抛出的异常
     * @title accessDeniedException
     * @author Jjcc
     * @param e 权限不足异常
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2020/4/1 13:38
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public AjaxResponse accessDeniedException(AccessDeniedException e){
        log.error("exception-------->", e);
        return AjaxResponse.error(new CustomException(CustomExceptionType.AUTHORIZATION_ERROR, "口令权限不足，无法访问"));
    }

    /**
     * 服务端字段验证异常返回
     * @title handlerBindException
     * @author Jjcc
     * @param ex
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/19 22:20
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public AjaxResponse handlerBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();

        return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, fieldError.getDefaultMessage()));
    }

    /**
     * 服务端字段验证异常返回
     * @title handlerBindException
     * @author Jjcc
     * @param ex
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/19 22:20
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public AjaxResponse handlerBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, fieldError.getDefaultMessage()));
    }

    /**
     * 异步请求超时异常
     * @title customAsyncRequestTimeoutException
     * @author Jjcc
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/24 9:09
     */
    @ExceptionHandler(CustomAsyncRequestTimeoutException.class)
    @ResponseBody
    public AjaxResponse customAsyncRequestTimeoutException(CustomAsyncRequestTimeoutException e) {
        return AjaxResponse.error(e);
    }

}



