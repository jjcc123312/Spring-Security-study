package com.security.springsecuritystudy.dao.mysqldao;

import com.security.springsecuritystudy.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jjcc
 * @since 2020-03-25
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 相关角色
     * @title findRoleByUserName
     * @author Jjcc
     * @param id 账号Id
     * @return java.util.List<java.lang.String>
     * @createTime 2020/3/25 15:41
     */
    @Select("select role.role_sn from user_role " +
            "left join user on `user`.id=user_role.user_id " +
            "left JOIN role on user_role.role_id=role.id " +
            "where user_id = #{id}")
    List<String> findRoleByUserName(@Param("id") Integer id);

    /**
     * 角色对应的权限
     * @title findAuthorityByRoleCode
     * @author Jjcc
     * @param roleCode 角色集合
     * @return java.util.List<java.lang.String>
     * @createTime 2020/3/25 15:56
     */
    List<String> findAuthorityByRoleCode(@Param("roleCode") List<String> roleCode);

}
