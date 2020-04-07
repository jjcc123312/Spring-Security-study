package com.security.springsecuritystudy.service;

import com.security.springsecuritystudy.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jjcc
 * @since 2020-03-25
 */
public interface UserService extends IService<User> {

    /**
     * 获取指定用户拥有的角色
     * @title findRoleByUserName
     * @author Jjcc
     * @param id 账号Id
     * @return java.util.List<java.lang.String>
     * @createTime 2020/3/25 15:34
     */
    List<String> findRoleByUserName(Integer id);

    /**
     * 获取指定角色拥有的权限
     * @title findAuthorityByRoleCode
     * @author Jjcc
     * @param roleCode 角色集合
     * @return java.util.List<java.lang.String>
     * @createTime 2020/3/25 15:54
     */
    List<String> findAuthorityByRoleCode(List<String> roleCode);

}
