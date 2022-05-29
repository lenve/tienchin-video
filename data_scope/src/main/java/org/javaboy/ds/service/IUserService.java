package org.javaboy.ds.service;

import org.javaboy.ds.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author javaboy
 * @since 2022-05-24
 */
public interface IUserService extends IService<User> {

    List<User> getAllUsers(User user);

}
