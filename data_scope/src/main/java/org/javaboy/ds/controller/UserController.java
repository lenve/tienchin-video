package org.javaboy.ds.controller;

import org.javaboy.ds.entity.Role;
import org.javaboy.ds.entity.User;
import org.javaboy.ds.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2022-05-24
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("/")
    public List<User> getAllUsers(User user) {
        return userService.getAllUsers(user);
    }

}
