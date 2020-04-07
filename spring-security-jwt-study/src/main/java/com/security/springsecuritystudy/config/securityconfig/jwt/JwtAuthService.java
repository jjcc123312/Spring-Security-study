package com.security.springsecuritystudy.config.securityconfig.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * token业务逻辑
 * @author Jjcc
 * @version 1.0.0
 * @className JwtAuthService.java
 * @createTime 2020年04月04日 13:35:00
 */
@Service
public class JwtAuthService {
    private AuthenticationManager authenticationManager;


    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthService(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 登录认证
     * @title login
     * @author Jjcc
     * @param username 账号
     * @param password 密码
     * @return java.lang.String
     * @createTime 2020/4/4 13:48
     */
    String login(String username, String password) {
        // 创建一个没有认证的 token
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 通过 authenticationManager 找到对应的 provider 进行认证处理；
        // 处理期间，可能会抛出
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetails principal = (UserDetails) authenticate.getPrincipal();

        //生成JWT
        return jwtTokenUtil.generatedToken(principal);
    }

    /**
     * 获取 headerName
     * @title getHedaerName
     * @author Jjcc
     * @return java.lang.String
     * @createTime 2020/4/5 16:16
     */
    public String getHedaerName() {
        return jwtTokenUtil.getHeader();
    }

    /**
     * 刷新token
     * @title refreshToken
     * @author Jjcc
     * @param oldToken 旧的token
     * @return java.lang.String
     * @createTime 2020/4/4 13:48
     */
    String refreshToken(String oldToken) {
        // 先判断当前token是否过期，没过期则刷新令牌
        if (!jwtTokenUtil.isTokenExpired(oldToken)) {
            return jwtTokenUtil.refreshToken(oldToken);
        }

        return null;
    }

}





