package org.javaboy.flowableprocess;

import org.flowable.common.engine.api.history.HistoricData;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.ProcessInstanceHistoryLog;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
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
public class ActHistoryTest {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    private static final Logger logger = LoggerFactory.getLogger(ActHistoryTest.class);

    /**
     * 查询一个已经执行完毕的流程的任务信息
     */
    @Test
    void test17() {
        //这个 list 就表示已经执行完毕的流程信息
        List<HistoricProcessInstance> list = historyService.createNativeHistoricProcessInstanceQuery().sql("SELECT RES.* , DEF.KEY_ as PROC_DEF_KEY_, DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_ as PROC_DEF_VERSION_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_ from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_ WHERE RES.END_TIME_ is not NULL order by RES.ID_ asc").list();
        for (HistoricProcessInstance hpi : list) {
            List<HistoricTaskInstance> taskInstances = historyService.createNativeHistoricTaskInstanceQuery().sql("SELECT RES.* from ACT_HI_TASKINST RES WHERE RES.PROC_INST_ID_ = #{pid} and RES.END_TIME_ is not null order by RES.ID_ asc")
                    .parameter("pid", hpi.getId()).list();
            for (HistoricTaskInstance hti : taskInstances) {
                logger.info("name:{},createTime:{},endTime:{},assignee:{}", hti.getName(), hti.getCreateTime(), hti.getEndTime(), hti.getAssignee());
            }
        }
    }

    /**
     * 查询某一个任务的处理人
     *
     * select * from ACT_HI_IDENTITYLINK where TASK_ID_ = ?
     *
     */
    @Test
    void test16() {
        String taskName = "组长审批";
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskName(taskName).singleResult();
        List<HistoricIdentityLink> links = historyService.getHistoricIdentityLinksForTask(task.getId());
        for (HistoricIdentityLink link : links) {
            logger.info("{} 任务的执行人是 {}", task.getName(), link.getUserId());
        }
    }

    /**
     * 查询某一个流程的处理人
     * <p>
     * select * from ACT_HI_IDENTITYLINK where PROC_INST_ID_ = ?
     */
    @Test
    void test15() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance hpi : list) {
            List<HistoricIdentityLink> links = historyService.getHistoricIdentityLinksForProcessInstance(hpi.getId());
            for (HistoricIdentityLink link : links) {
                logger.info("userId:{}", link.getUserId());
            }
        }
    }

    @Test
    void test14() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance pi : list) {
            ProcessInstanceHistoryLog pihl = historyService
                    //select RES.*, DEF.KEY_ as PROC_DEF_KEY_, DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_ as PROC_DEF_VERSION_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_ from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_ where PROC_INST_ID_ = ?
                    .createProcessInstanceHistoryLogQuery(pi.getId())
                    //增加查询历史任务信息
                    //SELECT RES.* from ACT_HI_TASKINST RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc
                    .includeTasks()
                    //SELECT RES.* from ACT_HI_VARINST RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc
                    .includeVariables()
                    //SELECT RES.* from ACT_HI_ACTINST RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc
                    .includeActivities()
                    .singleResult();
            //打印历史流程信息
            logger.info("id:{},startTime:{},endTime:{},startUserId:{}", pihl.getId(), pihl.getStartTime(), pihl.getEndTime(), pihl.getStartUserId());
            List<HistoricData> historicData = pihl.getHistoricData();
            for (HistoricData data : historicData) {
                if (data instanceof HistoricTaskInstance) {
                    HistoricTaskInstance hti = (HistoricTaskInstance) data;
                    //说明这是历史任务信息
                    logger.info("name:{},createTime:{},endTime:{},assignee:{}", hti.getName(), hti.getCreateTime(), hti.getEndTime(), hti.getAssignee());
                } else if (data instanceof HistoricActivityInstance) {
                    HistoricActivityInstance hai = (HistoricActivityInstance) data;
                    logger.info("activityName:{},startTime:{},endTime:{},assignee:{},activityType:{}", hai.getActivityName(), hai.getStartTime(), hai.getEndTime(), hai.getAssignee(), hai.getActivityType());
                } else if (data instanceof HistoricVariableInstance) {
                    HistoricVariableInstance hvi = (HistoricVariableInstance) data;
                    logger.info("variableName:{},createTime:{},variableTypeName:{},value:{}", hvi.getVariableName(), hvi.getCreateTime(), hvi.getVariableTypeName(), hvi.getValue());
                }
            }
        }
    }


    /**
     * 查询某一个流程的最终审批结果变量，即 state 的最终值
     * <p>
     * SELECT RES.* from ACT_HI_VARINST RES WHERE RES.PROC_INST_ID_ = ? and RES.NAME_ = ? order by RES.ID_ asc
     */
    @Test
    void test13() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance hpi : list) {
            List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(hpi.getId()).variableName("state").list();
            for (HistoricVariableInstance hvi : variableInstances) {
                logger.info("variableName:{},createTime:{},variableTypeName:{},value:{}", hvi.getVariableName(), hvi.getCreateTime(), hvi.getVariableTypeName(), hvi.getValue());
            }
        }
    }

    /**
     * 查询已经完成的流程实例的所有历史变量
     * <p>
     * SELECT RES.* from ACT_HI_VARINST RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc
     */
    @Test
    void test12() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance hpi : list) {
            List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(hpi.getId()).list();
            for (HistoricVariableInstance hvi : variableInstances) {
                logger.info("variableName:{},createTime:{},variableTypeName:{},value:{}", hvi.getVariableName(), hvi.getCreateTime(), hvi.getVariableTypeName(), hvi.getValue());
            }
        }
    }

    /**
     * 查询所有已经完成的 startEvent 活动
     * <p>
     * SELECT RES.* from ACT_HI_ACTINST RES WHERE RES.ACT_TYPE_ = ? and RES.END_TIME_ is not null order by RES.ID_ asc
     */
    @Test
    void test11() {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                //按照类型去查找活动
                .activityType("startEvent")
                .finished().list();
        for (HistoricActivityInstance hai : list) {
            logger.info("activityName:{},startTime:{},endTime:{},assignee:{},activityType:{}", hai.getActivityName(), hai.getStartTime(), hai.getEndTime(), hai.getAssignee(), hai.getActivityType());
        }
    }

    /**
     * 查询已经完成的流程的活动信息
     * <p>
     * 这个查询对应的表是 ACT_HI_ACTINST
     * <p>
     * SELECT RES.* from ACT_HI_ACTINST RES WHERE RES.PROC_INST_ID_ = ? order by START_TIME_ asc
     */
    @Test
    void test10() {
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance pi : processInstances) {
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    //按照流程实例 id 去查询
                    .processInstanceId(pi.getId())
                    //按照活动的开始时间排序
                    .orderByHistoricActivityInstanceStartTime()
                    .asc()
                    .list();
            for (HistoricActivityInstance hai : list) {
                logger.info("activityName:{},startTime:{},endTime:{},assignee:{},activityType:{}", hai.getActivityName(), hai.getStartTime(), hai.getEndTime(), hai.getAssignee(), hai.getActivityType());
            }
        }
    }

    /**
     * 查询某一个流程实例所有完成的任务
     * <p>
     * SELECT RES.* from ACT_HI_TASKINST RES WHERE RES.PROC_INST_ID_ = ? and RES.END_TIME_ is not null order by RES.ID_ asc
     */
    @Test
    void test09() {
        //查询所有已经完成的流程实例
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance hpi : list) {
            List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(hpi.getId()).finished().list();
            for (HistoricTaskInstance hti : taskInstances) {
                logger.info("name:{},createTime:{},endTime:{},assignee:{}", hti.getName(), hti.getCreateTime(), hti.getEndTime(), hti.getAssignee());
            }
        }
    }

    /**
     * 查询所有已完成的任务
     * <p>
     * SELECT RES.* from ACT_HI_TASKINST RES WHERE RES.END_TIME_ is not null order by RES.ID_ asc
     */
    @Test
    void test08() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().finished().list();
        for (HistoricTaskInstance hti : list) {
            logger.info("name:{},createTime:{},endTime:{},assignee:{}", hti.getName(), hti.getCreateTime(), hti.getEndTime(), hti.getAssignee());
        }
    }

    /**
     * 查询历史任务
     * <p>
     * 查询 zhangsan 已经完成的任务
     * <p>
     * SELECT RES.* from ACT_HI_TASKINST RES WHERE RES.ASSIGNEE_ = ? and RES.END_TIME_ is not null order by RES.ID_ asc
     */
    @Test
    void test07() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                //任务的处理人为 zhangsan  RES.ASSIGNEE_ = ?
                .taskAssignee("zhangsan")
                //任务已经完成 RES.END_TIME_ is not null
                .finished().list();
        for (HistoricTaskInstance hti : list) {
            logger.info("name:{},createTime:{},endTime:{},assignee:{}", hti.getName(), hti.getCreateTime(), hti.getEndTime(), hti.getAssignee());
        }
    }

    /**
     * SELECT RES.* , DEF.KEY_ as PROC_DEF_KEY_, DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_ as PROC_DEF_VERSION_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_ from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_ order by RES.ID_ asc
     * <p>
     * 也可以查询所有的流程信息，然后通过自行判断 endTime 字段是否为 null，来判断一个流程是否执行完毕
     */
    @Test
    void test06() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().list();
        for (HistoricProcessInstance hpi : list) {
            Date endTime = hpi.getEndTime();
            if (endTime != null) {
                //说明这个流程已经执行完毕
                logger.info("执行完毕：name:{},startTime:{},endTime:{}", hpi.getName(), hpi.getStartTime(), hpi.getEndTime());
            } else {
                //说明这个流程尚未执行
                logger.info("未执行完毕：name:{},startTime:{},endTime:{}", hpi.getName(), hpi.getStartTime(), hpi.getEndTime());
            }

        }
    }

    /**
     * 查询未执行完毕的流程信息
     * <p>
     * SELECT RES.* , DEF.KEY_ as PROC_DEF_KEY_, DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_ as PROC_DEF_VERSION_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_ from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_ WHERE RES.END_TIME_ IS NULL order by RES.ID_ asc
     */
    @Test
    void test05() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .unfinished()//END_TIME_ IS NULL
                .list();
        for (HistoricProcessInstance hpi : list) {
            logger.info("name:{},startTime:{},endTime:{}", hpi.getName(), hpi.getStartTime(), hpi.getEndTime());
        }
    }

    /**
     * 查询已经完成的历史流程信息，单纯的查询流程本身的信息
     * 流程的历史表中保存的流程信息，不仅有已经完成的流程信息，也包括正在执行的流程信息
     * <p>
     * SELECT RES.* , DEF.KEY_ as PROC_DEF_KEY_, DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_ as PROC_DEF_VERSION_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_ from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_ WHERE RES.END_TIME_ is not NULL order by RES.ID_ asc
     */
    @Test
    void test04() {
        List<HistoricProcessInstance> list = historyService
                //这个表示查询流程的历史信息（单纯的只是查询流程的信息）
                .createHistoricProcessInstanceQuery()
                //查询已经完成的流程信息
                .finished()//END_TIME_ is not NULL
                .list();
        for (HistoricProcessInstance hpi : list) {
            logger.info("name:{},startTime:{},endTime:{}", hpi.getName(), hpi.getStartTime(), hpi.getEndTime());
        }
    }

    @Test
    void test03() {
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("state", "审批通过");
        vars.put("days", 20);
        taskService.complete(task.getId(), vars);
    }

    @Test
    void test02() {
        Task task = taskService.createTaskQuery().taskAssignee("zhaoliu").singleResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("days", 10);
        vars.put("reason", "我想出去玩一下");
        vars.put("startTime", "2022-11-03");
        vars.put("endTime", "2022-11-13");
        taskService.complete(task.getId(), vars);
    }

    @Test
    void test01() {
        Authentication.setAuthenticatedUserId("zhaoliu");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("HistoryDemo01");
    }
}
