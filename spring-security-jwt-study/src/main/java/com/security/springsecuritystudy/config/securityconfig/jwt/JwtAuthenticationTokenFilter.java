package com.security.springsecuritystudy.config.securityconfig.jwt;

import com.security.springsecuritystudy.config.securityconfig.MyUserDetailsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt令牌鉴权过滤器
 * @author Jjcc
 * @version 1.0.0
 * @className JwtAuthenticationTokenFilter.java
 * @createTime 2020年04月04日 14:33:00
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private MyUserDetailsServiceImpl myUserDetailsService;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthenticationTokenFilter(MyUserDetailsServiceImpl myUserDetailsService,
                                        JwtTokenUtil jwtTokenUtil) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 从请求体的header中拿到 jwtToken
        String headerToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(headerToken)) {
            // 通过 jwtToken 拿到用户名
            String username = jwtTokenUtil.getUsernameFromToken(headerToken);
            // 判断用户名是否为空以及 判断当前的请求是否拥有 认证主体
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
                // 效验 jwtToken 包含的用户名 与 请求体中的用户名是否一致
                if (jwtTokenUtil.validateToken(headerToken, userDetails)) {
                    //给使用该JWT令牌的用户进行授权
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,null,
                            userDetails.getAuthorities());
                    // 将获取到的认证主体保存到 context 中
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}
