package org.javaboy.ds.mapper;

import org.javaboy.ds.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2022-05-24
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getAllRoles(Role role);
}
