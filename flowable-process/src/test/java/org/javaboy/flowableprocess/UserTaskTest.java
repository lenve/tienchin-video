package org.javaboy.flowableprocess;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
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
public class UserTaskTest {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(UserTaskTest.class);

    @Autowired
    IdentityService identityService;

    @Test
    void test20() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("g", "manager");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("UserTaskDemo",vars);
        logger.info("id:{},name:{}", pi.getId(), pi.getName());
    }

    @Test
    void test19() {
        Task task = taskService.createTaskQuery().taskCandidateGroup("manager").singleResult();
        taskService.claim(task.getId(), "zhangsan");
        taskService.complete(task.getId());
    }

    /**
     * 也可以根据候选用户组去查询一个任务
     * <p>
     * 对应的 SQL 如下：
     * <p>
     * SELECT RES.* from ACT_RU_TASK RES WHERE RES.ASSIGNEE_ is null and exists(select LINK.ID_ from ACT_RU_IDENTITYLINK LINK where LINK.TYPE_ = 'candidate'
     * and LINK.TASK_ID_ = RES.ID_ and ( ( LINK.GROUP_ID_ IN ( ? ) ) ) ) order by RES.ID_ asc
     * <p>
     * 这个查询一步到位，直接指定候选组即可
     */
    @Test
    void test18() {
        Task task = taskService.createTaskQuery().taskCandidateGroup("manager").singleResult();
        logger.info("name:{},createTime:{}", task.getName(), task.getCreateTime());
    }

    /**
     * 根据候选人去查询任务（可能 zhangsan 就是候选人，也可能 zhangsan 属于某一个或者某多个用户组，那么此时就需要先查询到 zhangsan 所属的用户组，然后再根据用户组去查询对应的任务）
     * <p>
     * 这个查询实际上分为两步：
     * <p>
     * 1. 执行的 SQL 如下：SELECT RES.* from ACT_ID_GROUP RES WHERE exists(select 1 from ACT_ID_MEMBERSHIP M where M.GROUP_ID_ = RES.ID_ and M.USER_ID_ = ?) order by RES.ID_ asc
     * 第一步这个 SQL 可以查询出来这个 zhangsan 所属的用户组
     * <p>
     * 2. 执行的 SQL 如下：
     * SELECT RES.* from ACT_RU_TASK RES WHERE RES.ASSIGNEE_ is null and
     * exists(select LINK.ID_ from ACT_RU_IDENTITYLINK LINK where LINK.TYPE_ = 'candidate' and LINK.TASK_ID_ = RES.ID_ and
     * ( LINK.USER_ID_ = ? or ( LINK.GROUP_ID_ IN ( ? ) ) ) ) order by RES.ID_ asc
     * <p>
     * 这个 SQL，本质上是查询 zhangsan 或者 zhangsan 所属的用户组的任务
     */
    @Test
    void test17() {
        Task task = taskService.createTaskQuery().taskCandidateUser("zhangsan").singleResult();
        logger.info("name:{},createTime:{}", task.getName(), task.getCreateTime());
    }

    /**
     * 创建 zhangsan 和 lisi 两个用户
     * 创建名为 g1 的用户组
     * 设置 zhangsan 和 lisi 都属于 g1
     */
    @Test
    void test16() {
        UserEntityImpl u1 = new UserEntityImpl();
        u1.setRevision(0);
        u1.setEmail("zhangsan@qq.com");
        u1.setPassword("123");
        u1.setId("zhangsan");
        u1.setDisplayName("张三");
        identityService.saveUser(u1);
        UserEntityImpl u2 = new UserEntityImpl();
        u2.setRevision(0);
        u2.setEmail("lisi@qq.com");
        u2.setPassword("123");
        u2.setId("lisi");
        u2.setDisplayName("李四");
        identityService.saveUser(u2);
        GroupEntityImpl g1 = new GroupEntityImpl();
        g1.setRevision(0);
        g1.setId("manager");
        g1.setName("经理");
        identityService.saveGroup(g1);
        identityService.createMembership("zhangsan", "manager");
        identityService.createMembership("lisi", "manager");
    }

    /**
     * 删除一个任务的候选人
     * <p>
     * 对应的 SQL 如下：
     * <p>
     * delete from ACT_RU_IDENTITYLINK where ID_ = ?
     * <p>
     * 实际上，是根据 wangwu + taskId 去 ACT_RU_IDENTITYLINK 表中查询到记录的详细信息 select * from ACT_RU_IDENTITYLINK where TASK_ID_ = ? and USER_ID_ = ? and TYPE_ = ?，然后再去删除
     */
    @Test
    void test15() {
        Task task = taskService.createTaskQuery().singleResult();
        taskService.deleteCandidateUser(task.getId(), "wangwu");
    }

    /**
     * 为任务增加候选人
     * <p>
     * 对应的 SQL 如下：
     * <p>
     * insert into ACT_RU_IDENTITYLINK (ID_, REV_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, PROC_INST_ID_, PROC_DEF_ID_, SCOPE_ID_, SUB_SCOPE_ID_, SCOPE_TYPE_, SCOPE_DEFINITION_ID_)
     * values (?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) , (?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
     */
    @Test
    void test14() {
        Task task = taskService.createTaskQuery().singleResult();
        taskService.addCandidateUser(task.getId(), "zhaoliu");
        taskService.addCandidateUser(task.getId(), "javaboy");
    }

    /**
     * 任务回退
     * <p>
     * 对应的 SQL 如下：
     * <p>
     * update ACT_RU_TASK SET REV_ = ?, ASSIGNEE_ = ? where ID_= ? and REV_ = ?
     */
    @Test
    void test13() {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("lisi").list();
        for (Task task : tasks) {
            //设置任务的处理人为 null，就表示任务回退
            taskService.setAssignee(task.getId(), null);
        }

    }

    @Test
    void test12() {
        Map<String, Object> vars = new HashMap<>();
        //设置多个处理用户，多个处理用户之间用 , 隔开
        vars.put("userIds", "zhangsan,lisi,wangwu");
        runtimeService.startProcessInstanceByKey("UserTaskDemo", vars);
    }

    @Test
    void test11() {
        taskService.claim("d8d0bfcf-4d43-11ed-86f3-acde48001122", "lisi");
    }

    /**
     * 认领任务
     * <p>
     * 这个认领，对应的 SQL：
     * <p>
     * update ACT_RU_TASK SET REV_ = ?, ASSIGNEE_ = ?, CLAIM_TIME_ = ? where ID_= ? and REV_ = ?
     */
    @Test
    void test10() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("zhangsan").list();
        for (Task task : tasks) {
            //认领，zhangsan 来认领这个任务
            taskService.claim(task.getId(), "zhangsan");
        }
    }

    /**
     * 根据流程的 ID 查询流程的参与者
     * <p>
     * 对应的 SQL：
     * <p>
     * select * from ACT_RU_IDENTITYLINK where PROC_INST_ID_ = ?
     */
    @Test
    void test09() {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().singleResult();
        //根据流程的 ID 去查询流程的参与者
        List<IdentityLink> links = runtimeService.getIdentityLinksForProcessInstance(pi.getId());
        for (IdentityLink link : links) {
            logger.info("流程参与人：{}", link.getUserId());
        }
    }

    /**
     * 根据候选人去查询任务
     * <p>
     * 对应的 SQL 如下：
     * <p>
     * SELECT RES.* from ACT_RU_TASK RES WHERE RES.ASSIGNEE_ is null and exists(select LINK.ID_ from ACT_RU_IDENTITYLINK LINK
     * where LINK.TYPE_ = 'candidate' and LINK.TASK_ID_ = RES.ID_ and ( LINK.USER_ID_ = ? ) ) order by RES.ID_ asc
     * <p>
     * 从 SQL 中 ，可以看到，任务由哪些用户来处理，主要是由 ACT_RU_IDENTITYLINK 表来维护。
     */
    @Test
    void test08() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("wangwu").list();
        for (Task task : tasks) {
            logger.info("name:{},CreateTime:{}", task.getName(), task.getCreateTime());
        }
    }

    /**
     * 第二种流程发起人的设置方式
     */
    @Test
    void test07() {
        identityService.setAuthenticatedUserId("fengqi");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("UserTaskDemo");
        logger.info("id:{},name:{}", pi.getId(), pi.getName());
    }

    /**
     * 启动一个流程，并设置流程的发起人
     * <p>
     * 流程的发起人有两种不同的设置方式：
     * <p>
     * 第一种：
     */
    @Test
    void test06() {
        //设置流程当前的处理人（一会流程启动的时候，会将这里设置的作为流程的发起人）
        Authentication.setAuthenticatedUserId("zhaoliu");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("UserTaskDemo");
        logger.info("id:{},name:{}", pi.getId(), pi.getName());
    }

    /**
     * 启动一个流程，在启动的时候，指定任务的处理人
     */
    @Test
    void test05() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("manager", "lisi");
        //在流程启动的时候，通过变量去指定任务的处理人
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("UserTaskDemo", vars);
        logger.info("id:{},name:{}", pi.getId(), pi.getName());
    }

    /**
     * 自己来处理任务
     */
    @Test
    void test04() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        for (Task task : list) {
            //查询到 zhangsan 的任务，并自己处理
            taskService.complete(task.getId());
        }
    }

    /**
     * 将 javaboy 需要处理的任务委派给 zhangsan 去处理
     * <p>
     * 具体的 SQL 如下：
     * <p>
     * update ACT_RU_TASK SET REV_ = ?, ASSIGNEE_ = ? where ID_= ? and REV_ = ?
     */
    @Test
    void test03() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        for (Task task : list) {
            //为某一个 Task 设置处理人
            taskService.setAssignee(task.getId(), "lisi");
        }
    }

    /**
     * 查询某一个用户需要处理的任务
     * <p>
     * 这个查询对应的 SQL：
     * <p>
     * SELECT RES.* from ACT_RU_TASK RES WHERE RES.ASSIGNEE_ = ? order by RES.ID_ asc
     */
    @Test
    void test02() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        for (Task task : list) {
            logger.info("name:{},assignee:{}", task.getName(), task.getAssignee());
        }
    }

    @Test
    void test01() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("UserTaskDemo");
        logger.info("id:{},name:{}", pi.getId(), pi.getName());
    }
}
