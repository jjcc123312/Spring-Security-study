package com.security.springsecuritystudy.config.securityconfig;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.springsecuritystudy.entity.User;
import com.security.springsecuritystudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 从数据库中获取用户信息需要实现 UserDetailsService 类
 * @author Jjcc
 * @version 1.0.0
 * @className MyUserDetailsService.java
 * @createTime 2020年03月25日 14:23:00
 */
@Component
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public MyUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        // 使用短信验证码登录时，为了快捷增加的条件 or mobile = username
        // 条件： where username = username or mobile = username
        QueryWrapper<User> name = userQueryWrapper.eq("username", username).or().eq("mobile", username);

        User user = userService.getOne(name);

        Optional<User> userOpt = Optional.ofNullable(user);
        userOpt.orElseThrow( () -> new UsernameNotFoundException("用户名不存在"));

        // 登录用户的角色
        List<String> roleByUserName = userService.findRoleByUserName(userOpt.get().getId());

        // 如果该用户没有对应的角色，直接返回
        if (null == roleByUserName || roleByUserName.size() == 0) {
            return userOpt.get();
        }

        // 角色对应的所有权限
        List<String> authorityByRoleCode = userService.findAuthorityByRoleCode(roleByUserName);

        // 为角色加上 ROLE_ 前缀， spring security 规范
        roleByUserName = roleByUserName.stream().map(rc -> "ROLE_" + rc).collect(Collectors.toList());

        // 在spring security中，角色是一种特殊的权限，需要与权限放置在一起
        authorityByRoleCode.addAll(roleByUserName);

        // 转成用逗号分隔的字符串，为用户设置权限标识
        userOpt.get().setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        String.join(",", authorityByRoleCode)
                )
        );

        return userOpt.get();
    }
}
