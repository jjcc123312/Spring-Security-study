package com.security.springsecuritystudy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.springsecuritystudy.config.exception.AjaxResponse;
import com.security.springsecuritystudy.config.exception.CustomException;
import com.security.springsecuritystudy.config.exception.CustomExceptionType;
import com.security.springsecuritystudy.entity.Address;
import com.security.springsecuritystudy.entity.SmsCode;
import com.security.springsecuritystudy.entity.User;
import com.security.springsecuritystudy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className SmsCodeController.java
 * @createTime 2020年03月30日 15:54:00
 */
@RestController
@Slf4j
public class SmsCodeController {

    private UserService userService;

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public SmsCodeController(UserService userService,
                             RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }



    /**
     * 获取短信验证码
     * @title getSms
     * @author Jjcc
     * @param mobile 手机号码
     * @param session session对象
     * @return com.security.springsecuritystudy.config.exception.AjaxResponse
     * @createTime 2020/3/30 16:23
     */
    @GetMapping("smsCode/{mobile}")
    public AjaxResponse getSms(@PathVariable String mobile, HttpSession session) {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile", mobile);

        User one = userService.getOne(userQueryWrapper);

        if (null == one) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR
                    , "您输入的手机号不是系统注册用户"));
        }

        // 创建一个 验证码对象
        SmsCode smsCode = new SmsCode(RandomStringUtils.randomNumeric(4), 60, mobile);
        System.out.println("smsCode：" + smsCode);

        //TODO 此处调用验证码发送服务接口
        log.info(smsCode.getCode() + "=》" + mobile);

        session.setAttribute("sms_key", smsCode);

        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        User user = new User();

        user.setId(1);
        user.setPassword("12331");

        valueOperations.set("sms_key", smsCode);

        return AjaxResponse.success("短信已发送到你的手机");
    }

    @GetMapping("/redisCacheCode")
    public AjaxResponse getRedisCode() {
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();

        Object smsKey = stringObjectValueOperations.get("sms_key");

        return AjaxResponse.success(smsKey);
    }
}








