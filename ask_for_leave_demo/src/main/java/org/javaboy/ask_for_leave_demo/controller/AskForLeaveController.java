package org.javaboy.ask_for_leave_demo.controller;

import org.javaboy.ask_for_leave_demo.model.AskForLeaveVO;
import org.javaboy.ask_for_leave_demo.model.RespBean;
import org.javaboy.ask_for_leave_demo.model.TaskVO;
import org.javaboy.ask_for_leave_demo.model.User;
import org.javaboy.ask_for_leave_demo.service.AskForLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@RestController
public class AskForLeaveController {

    @Autowired
    AskForLeaveService askForLeaveService;

    @PostMapping("/ask_for_leave")
    public RespBean askForLeave(@RequestBody AskForLeaveVO askForLeaveVO) {
        return askForLeaveService.askForLeave(askForLeaveVO);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return askForLeaveService.getAllUsers();
    }

    @GetMapping("/unapprove")
    public List<AskForLeaveVO> getUnapproveProcess() {
        return askForLeaveService.getUnapproveProcess();
    }

    @GetMapping("/image/{processId}")
    public void processImage(@PathVariable String processId, HttpServletResponse response) throws IOException {
        askForLeaveService.processImage(processId, response);
    }

    @GetMapping("/tasks")
    public List<TaskVO> getCurrentUserAllTask() {
        return askForLeaveService.getCurrentUserAllTask();
    }

    @PostMapping("/approve")
    public RespBean approve(@RequestBody TaskVO taskVO) {
        return askForLeaveService.approve(taskVO);
    }


    @GetMapping("/history")
    public List<AskForLeaveVO> geHistoryProcess() {
        return askForLeaveService.geHistoryProcess();
    }

}
