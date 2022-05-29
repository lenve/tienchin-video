package org.javaboy.ds.controller;

import org.javaboy.ds.entity.Dept;
import org.javaboy.ds.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2022-05-24
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    IDeptService deptService;

    @GetMapping("/")
    public List<Dept> getAllDepts(Dept dept) {
        return deptService.getAllDepts(dept);
    }
}
