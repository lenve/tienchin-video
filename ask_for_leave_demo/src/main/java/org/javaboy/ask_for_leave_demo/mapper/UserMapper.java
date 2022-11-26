package org.javaboy.ask_for_leave_demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.javaboy.ask_for_leave_demo.model.Role;
import org.javaboy.ask_for_leave_demo.model.User;

import java.util.List;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Mapper
public interface UserMapper {
    User loadUserByUsername(String username);

    List<Role> getUserRolesByUserId(Integer id);
}
