package org.javaboy.ask_for_leave_demo.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.javaboy.ask_for_leave_demo.model.AskForLeaveVO;
import org.javaboy.ask_for_leave_demo.model.RespBean;
import org.javaboy.ask_for_leave_demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Service
public class AskForLeaveService {

    @Autowired
    RuntimeService runtimeService;

    @Transactional
    public RespBean askForLeave(AskForLeaveVO askForLeaveVO) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("days", askForLeaveVO.getDays());
            vars.put("approveUser", askForLeaveVO.getApproveUser());
            vars.put("endTime", askForLeaveVO.getEndTime());
            vars.put("startTime", askForLeaveVO.getStartTime());
            vars.put("reason", askForLeaveVO.getReason());
            //当前登录用户
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //请假的申请人
            vars.put("applicant", user.getUsername());
            ProcessInstance pi = runtimeService.startProcessInstanceByKey("askforleave_demo", vars);
            return RespBean.ok("请假申请已提交");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("请假申请提交失败");
    }

}
