package com.security.springsecuritystudy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Jjcc
 */
@Controller
public class BizpageController {

    public static void main(String[] args){
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

    /**
     * 登录
     * @title index
     * @author Jjcc
     * @param username 账号
     * @param password 密码
     * @return java.lang.String
     * @createTime 2020/3/21 15:41
     */
//    @PostMapping("/login")
//    public String index(String username,String password) {
//        return "index";
//    }

    /**
     * 登录成功之后的首页
     * @title
     * @author Jjcc
     * @return 返回值
     * @createTime 2020/3/21 15:42
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 日志管理
     * @title showOrder
     * @author Jjcc
     * @return java.lang.String
     * @createTime 2020/3/21 15:42
     */
    @GetMapping("/syslog")
//    @PreAuthorize("hasRole('admin')")
    public String showOrder() {
        return "syslog";
    }

    /**
     * 用户管理
     * @title addOrder
     * @author Jjcc
     * @return java.lang.String
     * @createTime 2020/3/21 15:42
     */
    @GetMapping("/sysuser")
//    @PreAuthorize("hasRole('admin')")
    public String addOrder() {
        return "sysuser";
    }

    /**
     * 具体业务一
     * @title updateOrder
     * @author Jjcc
     * @return java.lang.String
     * @createTime 2020/3/21 15:42
     */
    @GetMapping("/biz1")
//    @PreAuthorize("hasAnyAuthority('ROLE_user', 'ROLE_admin')")
    public String updateOrder() {
        return "biz1";
    }

    /**
     * 具体业务二
     * @title deleteOrder
     * @author Jjcc
     * @return java.lang.String
     * @createTime 2020/3/21 15:42
     */
    @GetMapping("/biz2")
//    @PreAuthorize("hasAnyAuthority('ROLE_user', 'ROLE_admin')")
    public String deleteOrder() {
        return "biz2";
    }
}