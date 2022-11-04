package org.javaboy.flowableprocess;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class VariableTest {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(VariableTest.class);

    @Test
    void test16() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        Map<String, Object> transientVariables = new HashMap<>();
        transientVariables.put("aaaaa" , "bbbbbbbb");
        taskService.complete(task.getId(),null,transientVariables);
    }

    /**
     * 启动时候设置临时变量
     */
    @Test
    void test15() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("reason", "玩一下");
        vars.put("startTime", "2022-10-30");
        vars.put("endTime", "2022-11-09");
        //设置临时流程变量
        ProcessInstance pi = runtimeService.createProcessInstanceBuilder()
                .transientVariable("days", 10)
                .transientVariables(vars)
                .processDefinitionKey("VariableDemo")
                .start();
    }

    @Test
    void test14() {
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        Object a = runtimeService.getVariableLocal(task.getExecutionId(), "a");
        logger.info("a={}", a);
    }

    /**
     * 为某一个执行实例设置本地流程变量
     *
     * 这个是和某一个具体的执行实例绑定的，所以要根据执行实例来查询
     */
    @Test
    void test13() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("a", "b");
        runtimeService.setVariablesLocal(task.getExecutionId(),vars);
    }

    /**
     * 也可以在任务完成的时候设置本地流程变量
     */
    @Test
    void test12() {
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("state", "完成");
        //第三个变量为 true 就表示这是流程变量是一个本地流程变量
        taskService.complete(task.getId(), vars, true);
    }

    @Test
    void test11() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        //完成组长审批这个任务
        taskService.complete(task.getId());
    }

    /**
     * 查询一个本地流程变量
     *
     * : ==>  Preparing: select * from ACT_RU_VARIABLE WHERE TASK_ID_ = ? AND NAME_ = ?
     * : ==> Parameters: f59e6d52-584d-11ed-9b14-acde48001122(String), a(String)
     * : <==      Total: 1
     * : Flushing dbSqlSession
     * : flush summary: 0 insert, 0 update, 0 delete.
     * : now executing flush...
     * : --- GetTaskVariableCmd finished --------------------------------------------------------
     * : a=b
     *
     */
    @Test
    void test10() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        Object a = taskService.getVariableLocal(task.getId(), "a");
        logger.info("a={}", a);
    }

    /**
     * : ==>  Preparing: select * from ACT_RU_VARIABLE WHERE TASK_ID_ = ? AND NAME_ = ?
     * : ==> Parameters: f59e6d52-584d-11ed-9b14-acde48001122(String), a(String)
     * : <==      Total: 1
     * : Flushing dbSqlSession
     * : flush summary: 0 insert, 0 update, 0 delete.
     * : now executing flush...
     * : --- GetTaskVariableCmd finished --------------------------------------------------------
     * : a=b
     */
    @Test
    void test09() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        Object a = taskService.getVariable(task.getId(), "a");
        logger.info("a={}", a);
    }

    /**
     * 设置本地流程变量
     */
    @Test
    void test08() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("a", "b");
        vars.put("c", "d");
        taskService.setVariablesLocal(task.getId(),vars);
    }

    /**
     * 由于流程变量是和当前流程实例相关的，所以流程变量也可以直接通过流程实例来设置
     */
    @Test
    void test07() {
        List<Execution> list = runtimeService.createExecutionQuery().list();
        for (Execution execution : list) {
            runtimeService.setVariable(execution.getId(), "a", "b");
        }
    }

    /**
     * 完成任务时设置流程变量
     *
     * 由于流程要执行结束了，因此 ACT_RU_VARIABLE 表要被清空了，所以这里就只向 ACT_HI_VARINST 表中保存数据。
     */
    @Test
    void test06() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("state", "完成");
        taskService.complete(task.getId(),vars);
    }

    /**
     * 我们也可以根据 task 去查询流程变量
     *
     */
    @Test
    void test05() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        //这里即会根据 taskId 去查询，也会根据 taskId 对应的执行实例 id 去查询
        Object reason = taskService.getVariable(task.getId(), "reason");
        //这里也是先根据 taskId 先找到执行实例 id，然后根据执行实例的 id 去进行查询
        //select * from ACT_RU_VARIABLE WHERE EXECUTION_ID_ = ? AND TASK_ID_ is null
        Map<String, Object> variables = taskService.getVariables(task.getId());
        logger.info("reason:{},variables:{}", reason, variables);
    }

    /**
     * 通过 Task 来设置流程变量
     * <p>
     * 通过 Task 设置，也是插入到两个地方：
     * <p>
     * 1. ACT_HI_VARINST
     * 2. ACT_RU_VARIABLE
     * <p>
     * 在设置的时候，虽然需要传递 TaskId，但是并不是说这个变量跟当前 Task 绑定，通过这个 taskId 可以查询出来这个 Task 对应的 流程实例 id 和执行实例 id，将来插入的时候会用到
     */
    @Test
    void test04() {
        Task task = taskService.createTaskQuery().taskAssignee("javaboy").singleResult();
        logger.info("taskId:{}", task.getId());
        //第一个参数是 taskId，后面则是流程变量的 key-value
        taskService.setVariable(task.getId(), "result", "同意");
        Map<String, Object> vars = new HashMap<>();
        vars.put("a", "b");
        vars.put("c", "d");
        //批量设置流程变量
        taskService.setVariables(task.getId(), vars);
    }

    /**
     * 根据流程执行实例 ID 查询所有对应的流程变量：
     * <p>
     * select * from ACT_RU_VARIABLE WHERE EXECUTION_ID_ = ? AND TASK_ID_ is null
     */
    @Test
    void test03() {
        List<Execution> list = runtimeService.createExecutionQuery().list();
        for (Execution execution : list) {
            Map<String, Object> variables = runtimeService.getVariables(execution.getId());
            logger.info("variables:{}", variables);
        }
    }

    /**
     * 通过执行实例 ID  可以查询流程变量
     * <p>
     * 具体的查询 SQL：
     * <p>
     * select * from ACT_RU_VARIABLE WHERE EXECUTION_ID_ = ? AND TASK_ID_ is null AND NAME_ = ?
     */
    @Test
    void test02() {
        List<Execution> list = runtimeService.createExecutionQuery().list();
        for (Execution execution : list) {
            Object a = runtimeService.getVariable(execution.getId(), "a");
            logger.info("execution name：{},a:{}", execution.getName(), a);
        }
    }

    /**
     * 在流程启动的时候，就可以设置流程变量
     * <p>
     * 流程变量将被存入到两个地方：
     * <p>
     * 1. ACT_HI_VARINST：存入到历史信息表中，将来可以从历史表中查询到流程变量
     * <p>
     * insert into ACT_HI_VARINST (ID_, PROC_INST_ID_, EXECUTION_ID_, TASK_ID_, NAME_, REV_, VAR_TYPE_, SCOPE_ID_, SUB_SCOPE_ID_, SCOPE_TYPE_, BYTEARRAY_ID_, DOUBLE_, LONG_ , TEXT_, TEXT2_, CREATE_TIME_, LAST_UPDATED_TIME_) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * <p>
     * 2. ACT_RU_VARIABLE：流程运行的信息表，流程运行的变量将存入到这个表中
     * <p>
     * INSERT INTO ACT_RU_VARIABLE (ID_, REV_, TYPE_, NAME_, PROC_INST_ID_, EXECUTION_ID_, TASK_ID_, SCOPE_ID_, SUB_SCOPE_ID_, SCOPE_TYPE_, BYTEARRAY_ID_, DOUBLE_, LONG_ , TEXT_, TEXT2_) VALUES ( ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     */
    @Test
    void test01() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("days", 10);
        vars.put("reason", "玩一下");
        vars.put("startTime", "2022-10-30");
        vars.put("endTime", "2022-11-09");
        //这个就是在启动的时候设置流程变量，这里设置的流程变量是一个全局的流程变量
        runtimeService.startProcessInstanceByKey("VariableDemo", vars);
    }
}
