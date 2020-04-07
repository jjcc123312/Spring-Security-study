package com.security.springsecuritystudy.config.async;

import com.security.springsecuritystudy.config.exception.CustomAsyncRequestTimeoutException;
import com.security.springsecuritystudy.config.exception.CustomExceptionType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * 请求超时策略
 * @author Jjcc
 * @version 1.0.0
 * @className RequestTimeCallableProcessor.java
 * @createTime 2019年10月23日 23:25:00
 */
public class RequestTimeCallableProcessor implements CallableProcessingInterceptor {

    @Override
    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) {
        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String requestURI = nativeRequest.getRequestURI();

        return new CustomAsyncRequestTimeoutException(CustomExceptionType.REQUEST_TIME_OUT.getCode(), requestURI, "请求超时");
    }
}
