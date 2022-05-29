package org.javaboy.ds.service;

import org.javaboy.ds.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author javaboy
 * @since 2022-05-24
 */
public interface IRoleService extends IService<Role> {

    List<Role> getAllRoles(Role role);
}
