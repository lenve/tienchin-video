package org.javaboy.permiss_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@RestController
public class UserController {

    @RequestMapping("/add")
//    @PreAuthorize("hasPermission('/add','system:user:add')")
//    @PreAuthorize("hasAuthority('system:user:add')")
    @PreAuthorize("hasPermission('system:user:add')")
    public String add() {
        return "add";
    }

    @RequestMapping("/delete")
//    @PreAuthorize("hasPermission('/delete','system:user:delete')")
//    @PreAuthorize("hasAuthority('system:user:delete')")
    @PreAuthorize("hasAnyPermissions('system:user:add','system:user:delete')")
    public String delete() {
        return "delete";
    }

    @RequestMapping("/update")
//    @PreAuthorize("hasPermission('/update','system:user:update')")
//    @PreAuthorize("hasAuthority('system:user:update')")
    @PreAuthorize("hasAllPermissions('system:user:add','system:user:delete')")
    public String update() {
        return "update";
    }

    @RequestMapping("/select")
//    @PreAuthorize("hasPermission('/select','system:user:select')")
//    @PreAuthorize("hasAuthority('system:user:select')")
    @PreAuthorize("hasAllPermissions('system:user:add','system:user:delete','system:user:select')")
    public String select() {
        return "select";
    }
}
