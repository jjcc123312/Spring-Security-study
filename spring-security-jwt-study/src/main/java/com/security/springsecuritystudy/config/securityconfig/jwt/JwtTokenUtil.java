package com.security.springsecuritystudy.config.securityconfig.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT令牌工具类
 * @author Jjcc
 * @version 1.0.0
 * @className JwtTokenUtil.java
 * @createTime 2020年04月03日 20:41:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenUtil {

    /**
     * http请求体体重header中的name
     */
    private String header;

    /**
     * jwt的基础信息加密和解密的密匙
     */
    private String secret;

    /**
     * 令牌存活时间
     */
    private Long expiration;


    /**
     * 生成令牌
     * @title generatedToken
     * @author Jjcc
     * @param userDetails 用户信息对象的父类
     * @return java.lang.String
     * @createTime 2020/4/3 20:57
     */
    String generatedToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userDetails.getUsername());
        claims.put("create", new Date());
        return generatedToken(claims);
    }

    /**
     * 通过 claims 生成令牌
     * @title generatedToken
     * @author Jjcc
     * @param claims 数据声明
     * @return java.lang.String
     * @createTime 2020/4/3 20:58
     */
    private String generatedToken(Map<String, Object> claims) {
        // 令牌过期时间
        Date date = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder().setClaims(claims)
                // 过期时间
                .setExpiration(date)
                // 不使用公匙，私匙
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从令牌中获取用户名
     * @title getUsernameFromToken
     * @author Jjcc
     * @param token 令牌
     * @return java.lang.String
     * @createTime 2020/4/3 21:11
     */
    String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claimsFromToken = getClaimsFromToken(token);
            username = claimsFromToken.getSubject();
        } catch (Exception e) {
            username = null;
        }

        return username;
    }

    /**
     * 从令牌中获取数据声明
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 令牌是否过期
     * @title isTokenExpired
     * @author Jjcc
     * @param token 令牌
     * @return java.lang.Boolean true：token过期了；false：没过期
     * @createTime 2020/4/3 21:15
     */
    Boolean isTokenExpired(String token) {
        try {
            Claims claimsFromToken = getClaimsFromToken(token);
            Date expiration = claimsFromToken.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     * @title refreshToken
     * @author Jjcc
     * @param token 令牌
     * @return java.lang.String
     * @createTime 2020/4/3 23:21
     */
    String refreshToken(String token){
        String refreshToken;
        try {
            Claims claimsFromToken = getClaimsFromToken(token);
            claimsFromToken.put("create", new Date());
            refreshToken = generatedToken(claimsFromToken);
        } catch (Exception e) {
            refreshToken = null;
        }
        return refreshToken;
    }

    /**
     * 验证令牌是否相同以及是否过期
     * @title validateToken
     * @author Jjcc
     * @param token 令牌
     * @param userDetails 用户信息
     * @return java.lang.Boolean
     * @createTime 2020/4/3 23:29
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


}
