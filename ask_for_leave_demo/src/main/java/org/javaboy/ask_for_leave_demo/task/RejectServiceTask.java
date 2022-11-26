package org.javaboy.ask_for_leave_demo.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RejectServiceTask implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(RejectServiceTask.class);

    @Override
    public void execute(DelegateExecution execution) {
        Map<String, Object> variables = execution.getVariables();
        //提交请假申请的用户名
        Object name = variables.get("name");
        //请假的天数
        Object days = variables.get("days");
        logger.error("{} 请假 {} 天的申请没有通过审批", name, days);
    }
}
