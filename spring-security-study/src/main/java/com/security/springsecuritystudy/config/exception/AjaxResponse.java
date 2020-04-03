package com.security.springsecuritystudy.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className AjaxResponse.java
 * @createTime 2019年10月17日 23:24:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResponse {

    /**
     * ajax请求是否成功
     */
    private boolean isok;
    /**
     * http status code
     */
    private int code;
    /**
     * 请求失败的的提示信息。
     */
    private String message;
    /**
     * 请求成功时，需要响应给前端的数据
     */
    private Object data;



    /**
     * 请求出现异常时的响应
     * @title error
     * @author Jjcc
     * @param e
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/17 23:28
     */
    public static AjaxResponse error(CustomException e) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setCode(e.getCode());
        if(e.getCode() == CustomExceptionType.USER_INPUT_ERROR.getCode()){
            resultBean.setMessage(e.getMessage());
        } else if(e.getCode() == CustomExceptionType.SYSTEM_ERROR.getCode()){
            resultBean.setMessage(e.getMessage() + ",系统出现异常，请联系管理员电话：17673640083进行处理!");
        } else if(e.getCode() == CustomExceptionType.AUTHORIZATION_ERROR.getCode()) {
            resultBean.setMessage(e.getMessage());
        } else if (e.getCode() == CustomExceptionType.CAPTCHACODE_ERROR.getCode()) {
            resultBean.setMessage(e.getMessage());
        } else{
            resultBean.setMessage("系统出现未知异常，请联系管理员电话：17673640083进行处理!");
        }
        return resultBean;
    }


    /**
     * 异步请求超时
     * @title error
     * @author Jjcc
     * @param e
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/24 9:22
     */
    public static AjaxResponse error(CustomAsyncRequestTimeoutException e) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setCode(e.getCode());
        resultBean.setMessage(e.getUri() + "，请求超时");
        return resultBean;
    }

    /**
     * 请求成功时的响应数据封装，没有响应数据
     * @title success
     * @author Jjcc
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/17 23:30
     */
    public static AjaxResponse success() {
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(true);
        ajaxResponse.setCode(200);
        ajaxResponse.setMessage("成功");
        return ajaxResponse;
    }

    /**
     * 请求成功时的响应数据封装,有响应数据
     * @title success
     * @author Jjcc 
     * @param o 返回给前端的数据
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2019/10/17 23:32
     */
    public static AjaxResponse success(Object o) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(true);
        ajaxResponse.setCode(200);
        ajaxResponse.setMessage("成功");
        ajaxResponse.setData(o);

        return ajaxResponse;
    }
}
