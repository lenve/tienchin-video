package org.javaboy.ask_for_leave_demo.controller;

import org.javaboy.ask_for_leave_demo.model.AskForLeaveVO;
import org.javaboy.ask_for_leave_demo.model.RespBean;
import org.javaboy.ask_for_leave_demo.service.AskForLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class AskForLeaveController {

    @Autowired
    AskForLeaveService askForLeaveService;

    @PostMapping("/ask_for_leave")
    public RespBean askForLeave(@RequestBody AskForLeaveVO askForLeaveVO) {
        return askForLeaveService.askForLeave(askForLeaveVO);
    }
}
