package com.security.springsecuritystudy.service.impl;

import com.security.springsecuritystudy.entity.User;
import com.security.springsecuritystudy.dao.mysqldao.UserMapper;
import com.security.springsecuritystudy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jjcc
 * @since 2020-03-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<String> findRoleByUserName(Integer id) {
        return userMapper.findRoleByUserName(id);
    }

    @Override
    public List<String> findAuthorityByRoleCode(List<String> roleCode) {
        return userMapper.findAuthorityByRoleCode(roleCode);
    }
}
