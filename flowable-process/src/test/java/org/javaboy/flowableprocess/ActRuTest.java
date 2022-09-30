package org.javaboy.flowableprocess;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */

import com.datical.liquibase.ext.storedlogic.trigger.change.EnableTriggerStatement;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.agenda.TakeOutgoingSequenceFlowsOperation;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 流程运行之后，在流程运行的过程中，涉及到的表都是以 ACT_RU_ 前缀开头的
 * <p>
 * 需要注意的是，当一个流程执行完成后，在 ACT_RU_  前缀开始的表中，这个流程的所有数据都会被清除掉
 */
@SpringBootTest
public class ActRuTest {

    //跟流程运行相关的操作，都在这个 Bean 中，在 Spring Boot 中，该 Bean 已经配置好，可以直接使用
    @Autowired
    RuntimeService runtimeService;
    private static final Logger logger = LoggerFactory.getLogger(ActRuTest.class);
    @Autowired
    IdentityService identityService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    TaskService taskService;

    /**
     *
     * 删除一个正在执行的流程，注意这个只会删除正在执行的流程实例信息，并不会删除历史流程信息（历史信息被更新）。
     *
     * : ==>  Preparing: delete from ACT_RU_VARIABLE where EXECUTION_ID_ = ?
     * : ==> Parameters: 87df7272-3cad-11ed-9026-acde48001122(String)
     * : <==    Updates: 1
     * : ==>  Preparing: delete from ACT_RU_IDENTITYLINK where PROC_INST_ID_ = ?
     * : ==> Parameters: 87df7272-3cad-11ed-9026-acde48001122(String)
     * : <==    Updates: 2
     * : ==>  Preparing: delete from ACT_RU_TASK where ID_ = ? and REV_ = ?
     * : ==> Parameters: 87e4c9a9-3cad-11ed-9026-acde48001122(String), 1(Integer)
     * : <==    Updates: 1
     * : ==>  Preparing: delete from ACT_RU_TASK where EXECUTION_ID_ = ?
     * : ==> Parameters: 87df7272-3cad-11ed-9026-acde48001122(String)
     * : <==    Updates: 0
     * : ==>  Preparing: delete from ACT_RU_ACTINST where PROC_INST_ID_ = ?
     * : ==> Parameters: 87df7272-3cad-11ed-9026-acde48001122(String)
     * : <==    Updates: 3
     * : ==>  Preparing: delete from ACT_RU_ACTINST where PROC_INST_ID_ = ?
     * : ==> Parameters: 87df7272-3cad-11ed-9026-acde48001122(String)
     * : <==    Updates: 0
     * : ==>  Preparing: delete from ACT_RU_EXECUTION where ID_ = ? and REV_ = ?
     * : ==> Parameters: 87e035c5-3cad-11ed-9026-acde48001122(String), 2(Integer)
     * : <==    Updates: 1
     * : ==>  Preparing: delete from ACT_RU_EXECUTION where ID_ = ? and REV_ = ?
     * : ==> Parameters: 87df7272-3cad-11ed-9026-acde48001122(String), 2(Integer)
     * : <==    Updates: 1
     */
    @Test
    void test06() {
        String processInstanceId = "87df7272-3cad-11ed-9026-acde48001122";
        String deleteReason = "想删除了";
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    /**
     * 查看运行活动节点，本质上其实就是查看 ACT_RU_EXECUTION 表
     */
    @Test
    void test05() {
        List<Execution> executions = runtimeService.createExecutionQuery().list();
        for (Execution execution : executions) {
            //查询某一个执行实例的活动节点
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(execution.getId());
            for (String activeActivityId : activeActivityIds) {
                //这里拿到的其实就是 ACT_RU_EXECUTION 表中的 ACT_ID_ 字段
                logger.info("activeActivityId:{}", activeActivityId);
            }
        }
    }

    /**
     * 查询一个流程是否执行结束
     * 如果 ACT_RU_EXECUTION 表中，由于关于这个流程的记录，说明这个流程还在执行中
     * 如果 ACT_RU_EXECUTION 表中，没有关于这个流程的记录，说明这个流程已经执行结束了
     *
     * 注意，虽然我们是去 ACT_RU_EXECUTION 表中查询，且该表中同一个流程实例 ID 对应了多条记录，但是大家注意，这里查询到的其实还是一个流程实例。
     *
     */
    @Test
    void test04() {
        String processId = "cc189d50-3cac-11ed-8459-acde48001122";
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if (pi == null) {
            logger.info("流程执行结束");
        }else{
            logger.info("流程正在执行中");
        }
    }

    /**
     * 查询 wangwu 需要执行的任务，并处理
     *
     * 查询 wangwu 需要处理的任务，对应的 SQL：
     *
     * : ==>  Preparing: SELECT RES.* from ACT_RU_TASK RES WHERE RES.ASSIGNEE_ = ? order by RES.ID_ asc
     * : ==> Parameters: wangwu(String)
     * : <==      Total: 1
     *
     * wangwu 去处理任务：
     *
     * 首先去 ACT_RU_TASK 表中添加一条记录，这个新的记录，就是主管审批。
     * 然后从 ACT_RU_TASK 表中删除掉之前的需要 wangwu 完成的记录。
     *
     * 上面这两个操作是在同一个事务中完成的。
     *
     */
    @Test
    void test03() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("javaboy").list();
        for (Task task : list) {
            logger.info("id:{},assignee:{},name:{}",task.getId(),task.getAssignee(),task.getName());
            //王五完成自己的任务
            taskService.complete(task.getId());
        }
    }

    /**
     * 另外一种流程启动的例子和流程发起人设置的例子
     */
    @Test
    void test02() {
        //设置流程的发起人
        identityService.setAuthenticatedUserId("wangwu");
        //查询最新版本的 leave 流程的定义信息
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").latestVersion().singleResult();
        //也可以通过流程定义的 id 去启动一个流程，但是需要注意，流程定义的 id 需要自己去查询，这个 id 并不是 XML 文件中定义的流程 ID
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        logger.info("definitionId:{},id:{},name:{}", pi.getProcessDefinitionId(), pi.getId(), pi.getName());
    }

    @Test
    void test01() {
        //设置流程的发起人
        Authentication.setAuthenticatedUserId("wangwu");
        //这个流程定义的 key 实际上就是流程 XML 文件中的流程 id
        String processDefinitionKey = "leave";
        //流程启动成功之后，可以获取到一个流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        logger.info("definitionId:{},id:{},name:{}", pi.getProcessDefinitionId(), pi.getId(), pi.getName());
        //也可以通过流程定义的 id 去启动一个流程，但是需要注意，流程定义的 id 需要自己去查询，这个 id 并不是 XML 文件中定义的流程 ID
//        String processDefinitionId = "";
//        runtimeService.startProcessInstanceById(processDefinitionId);
    }


}
