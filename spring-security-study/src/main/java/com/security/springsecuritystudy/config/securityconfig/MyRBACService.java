package com.security.springsecuritystudy.config.securityconfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className MyRBACService.java
 * @createTime 2020年03月27日 13:45:00
 */
@Component("rabcService")
public class MyRBACService {

    /**
     * 判断某用户是否具有该request资源的访问权限
     * @title hasPermission
     * @author Jjcc
     * @param request 请求体对象
     * @param authentication 认证对象
     * @return boolean
     * @createTime 2020/3/27 13:49
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            // 将 请求地址转化为权限集合中的元素
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(request.getRequestURI());

            // 当前登录账号拥有的权限
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            // 如果urls列表中任何一个元素，能够和request.getRequestURI()请求资源路径相匹配，则表示该用户具有访问该资源的权限。
            return authorities.contains(grantedAuthorities.get(0));
        }

        return false;
    }
}
