package org.javaboy.flowableprocess;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
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
public class GatewayTest {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Test
    void test05() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("money", 200);
        runtimeService.startProcessInstanceByKey("InclusiveGatewayDemo01",vars);
    }

    @Test
    void test04() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("wangwu").list();
        for (Task task : list) {
            taskService.complete(task.getId());
        }
    }

    @Test
    void test03() {
        runtimeService.startProcessInstanceByKey("ParallelGatewayDemo01");
    }


    @Test
    void test02() {
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {
            taskService.complete(task.getId());
        }
    }

    @Test
    void test01() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("days", 1);
        runtimeService.startProcessInstanceByKey("ExclusiveGatewayDemo01", vars);
    }
}
