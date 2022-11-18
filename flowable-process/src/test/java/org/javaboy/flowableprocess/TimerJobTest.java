package org.javaboy.flowableprocess;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
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
@SpringBootTest
public class TimerJobTest {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    TaskService taskService;

    @Autowired
    ManagementService managementService;

    @Test
    void test08() {
        //将私信队列中的记录移动到 ACT_RU_JOB 表中，移动成功之后，这个定时任务就会立马被执行
        managementService.moveDeadLetterJobToExecutableJob("579d726c-5dc5-11ed-a105-acde48001122", 3);
    }

    @Test
    void test07() {
        repositoryService.activateProcessDefinitionByKey("ExclusiveGatewayDemo01", true, null);
    }

    @Test
    void test06() {
        //将 ACT_RU_TIMER_JOB 表中的定时任务移动到 ACT_RU_JOB 中，ACT_RU_JOB 表中的任务被扫描到之后，就会立马执行
        managementService.moveTimerToExecutableJob("14309833-5dc6-11ed-b1d6-acde48001122");
    }

    @Test
    void test05() {
        //将 ACT_RU_TIMER_JOB 表中的定时任务移动到 ACT_RU_DEADLETTER_JOB 表中，此时这个定时任务将不会再执行了
        managementService.moveJobToDeadLetterJob("579d726c-5dc5-11ed-a105-acde48001122");
    }

    @Test
    void test04() {
        //这个方法可以定时激活，激活的参数和挂起的参数基本一致
        repositoryService.activateProcessDefinitionByKey("ExclusiveGatewayDemo01", true, new Date(System.currentTimeMillis() + 20 * 1000));
    }

    @Test
    void test03() {
        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());
    }

    @Test
    void test02() {
        //定时挂起流程定义包括流程实例
        // 60s 之后，会挂起 ExclusiveGatewayDemo01 流程定义以及它所对应的流程实例
        repositoryService.suspendProcessDefinitionByKey("ExclusiveGatewayDemo01", true, new Date(System.currentTimeMillis() + 240 * 1000));
    }

    @Test
    void test01() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("days", 10);
        runtimeService.startProcessInstanceByKey("ExclusiveGatewayDemo01", vars);
    }
}
