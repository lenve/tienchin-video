package org.javaboy.ds.service.impl;

import org.javaboy.ds.annoataion.DataScope;
import org.javaboy.ds.entity.Role;
import org.javaboy.ds.mapper.RoleMapper;
import org.javaboy.ds.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2022-05-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<Role> getAllRoles(Role role) {
        return roleMapper.getAllRoles(role);
    }
}
