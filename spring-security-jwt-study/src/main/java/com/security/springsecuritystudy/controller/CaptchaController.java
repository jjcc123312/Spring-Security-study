package com.security.springsecuritystudy.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.security.springsecuritystudy.entity.CaptchaImageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * 获取验证码
 * @author Jjcc
 * @version 1.0.0
 * @className CaptchaController.java
 * @createTime 2020年03月29日 16:08:00
 */
@RestController
@Slf4j
public class CaptchaController {

    @Resource
    DefaultKaptcha captchaProducer;

    /**
     * 获取验证码
     * @title kaptcha
     * @author Jjcc
     * @param session session对象
     * @param response 响应对象
     * @return void
     * @createTime 2020/3/29 16:08
     */
    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void kaptcha(HttpSession session, HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        CaptchaImageVO captchaImageVO = new CaptchaImageVO(capText,2 * 60);
        //将验证码存到session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, captchaImageVO);

        //将图片返回给前端
        try(ServletOutputStream out = response.getOutputStream();) {
            BufferedImage bi = captchaProducer.createImage(capText);
            ImageIO.write(bi, "jpg", out);
            out.flush();
        }//使用try-with-resources不用手动关闭流
    }
}
