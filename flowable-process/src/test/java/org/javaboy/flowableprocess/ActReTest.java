package org.javaboy.flowableprocess;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
@SpringBootTest
public class ActReTest {
    @Autowired
    RepositoryService repositoryService;
    private static final Logger logger = LoggerFactory.getLogger(ActReTest.class);


    /**
     * 删除一个流程部署
     */
    @Test
    void test09() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : list) {
            repositoryService.deleteDeployment(deployment.getId());
        }
    }

    /**
     * 也可以自定义流程部署的查询 SQL
     */
    @Test
    void test08() {
        List<Deployment> list = repositoryService.createNativeDeploymentQuery()
                .sql("SELECT RES.* from ACT_RE_DEPLOYMENT RES WHERE RES.CATEGORY_ = #{category} order by RES.ID_ asc")
                .parameter("category", "我的流程分类")
                .list();
        for (Deployment d : list) {
            logger.info("id:{},category:{},,name:{},key:{}", d.getId(), d.getCategory(), d.getName(), d.getKey());
        }
    }

    /**
     * 根据流程部署的 ID 去查询流程定义
     * <p>
     * : ==>  Preparing: SELECT RES.* from ACT_RE_DEPLOYMENT RES order by RES.ID_ asc
     * : ==> Parameters:
     * : <==      Total: 2
     * : Flushing dbSqlSession
     * : flush summary: 0 insert, 0 update, 0 delete.
     * : now executing flush...
     * : --- DeploymentQueryImpl finished --------------------------------------------------------
     * : --- starting ProcessDefinitionQueryImpl --------------------------------------------------------
     * : Running command with propagation REQUIRED
     * : Operation class org.flowable.engine.impl.interceptor.CommandInvoker$1 added to agenda
     * : Executing operation class org.flowable.engine.impl.interceptor.CommandInvoker$1
     * : ==>  Preparing: SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.DEPLOYMENT_ID_ = ? order by RES.ID_ asc
     * : ==> Parameters: 9a5d421d-3c95-11ed-99f7-acde48001122(String)
     * : <==      Total: 1
     * : Flushing dbSqlSession
     * : flush summary: 0 insert, 0 update, 0 delete.
     * : now executing flush...
     * : --- ProcessDefinitionQueryImpl finished --------------------------------------------------------
     * : id:9b09aec0-3c95-11ed-99f7-acde48001122,name:javaboy的报销流程,version:1,category:http://www.flowable.org/processdef
     * : --- starting ProcessDefinitionQueryImpl --------------------------------------------------------
     * : Running command with propagation REQUIRED
     * : Operation class org.flowable.engine.impl.interceptor.CommandInvoker$1 added to agenda
     * : Executing operation class org.flowable.engine.impl.interceptor.CommandInvoker$1
     * : ==>  Preparing: SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.DEPLOYMENT_ID_ = ? order by RES.ID_ asc
     * : ==> Parameters: a31bea4a-3c96-11ed-9ebe-acde48001122(String)
     * : <==      Total: 1
     * : Flushing dbSqlSession
     * : flush summary: 0 insert, 0 update, 0 delete.
     * : now executing flush...
     * : --- ProcessDefinitionQueryImpl finished --------------------------------------------------------
     * : id:a3d7e74d-3c96-11ed-9ebe-acde48001122,name:javaboy的报销流程666,version:2,category:http://www.flowable.org/processdef
     */
    @Test
    void test07() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment d : list) {
            List<ProcessDefinition> list1 = repositoryService.createProcessDefinitionQuery().deploymentId(d.getId()).list();
            for (ProcessDefinition pd : list1) {
                logger.info("id:{},name:{},version:{},category:{}", pd.getId(), pd.getName(), pd.getVersion(), pd.getCategory());
            }
        }
    }

    /**
     * 根据流程部署的分类，去查询流程部署信息
     * <p>
     * : ==>  Preparing: SELECT RES.* from ACT_RE_DEPLOYMENT RES WHERE RES.CATEGORY_ = ? order by RES.ID_ asc
     * : ==> Parameters: 我的流程分类(String)
     * : <==      Total: 2
     */
    @Test
    void test06() {
        List<Deployment> list = repositoryService.createDeploymentQuery()
                //根据流程部署的分类去查询
                .deploymentCategory("我的流程分类")
                .list();
        for (Deployment d : list) {
            logger.info("id:{},category:{},,name:{},key:{}", d.getId(), d.getCategory(), d.getName(), d.getKey());
        }
    }


    /**
     * 查询流程部署信息，本质上就是查询 ACT_RE_DEPLOYMENT 表
     * <p>
     * : ==>  Preparing: SELECT RES.* from ACT_RE_DEPLOYMENT RES order by RES.ID_ asc
     * : ==> Parameters:
     * : <==      Total: 2
     */
    @Test
    void test05() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment d : list) {
            logger.info("id:{},category:{},,name:{},key:{}", d.getId(), d.getCategory(), d.getName(), d.getKey());
        }
    }

    /**
     * 根据流程定义的 key 去查询
     */
    @Test
    void test04() {
        List<ProcessDefinition> list = repositoryService.createNativeProcessDefinitionQuery()
                .sql("SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.KEY_ = #{key} order by RES.VERSION_ desc")
                .parameter("key", "javaboy_submit_an_expense_account")
                .list();
        for (ProcessDefinition pd : list) {
            logger.info("id:{},name:{},version:{},category:{}", pd.getId(), pd.getName(), pd.getVersion(), pd.getCategory());
        }
    }

    /**
     * 根据流程定义的 key 去查询
     * <p>
     * : ==>  Preparing: SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.KEY_ = ? order by RES.VERSION_ desc
     * : ==> Parameters: javaboy_submit_an_expense_account(String)
     * : <==      Total: 2
     */
    @Test
    void test03() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                //根据流程定义的 XML 文件中的 id 去查询一个流程，注意，XML 文件中的 id 对应 ACT_RE_PROCDEF 表中的 KEY_
                .processDefinitionKey("javaboy_submit_an_expense_account")
                //按照版本号排序
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition pd : list) {
            logger.info("id:{},name:{},version:{},category:{}", pd.getId(), pd.getName(), pd.getVersion(), pd.getCategory());
        }
    }


    /**
     * 查询所有流程的最新版本
     * <p>
     * 对应的 SQL：
     * <p>
     * : ==>  Preparing: SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.VERSION_ = (select max(VERSION_) from ACT_RE_PROCDEF where KEY_ = RES.KEY_ and ( (TENANT_ID_ IS NOT NULL and TENANT_ID_ = RES.TENANT_ID_) or (TENANT_ID_ IS NULL and RES.TENANT_ID_ IS NULL) ) ) order by RES.ID_ asc
     * : ==> Parameters:
     * : <==      Total: 1
     */
    @Test
    void test02() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                //设置查询流程定义的最新版本
                .latestVersion()
                .list();
        for (ProcessDefinition pd : list) {
            logger.info("id:{},name:{},version:{},category:{}", pd.getId(), pd.getName(), pd.getVersion(), pd.getCategory());
        }
    }

    /**
     * 查询流程定义
     * <p>
     * 对应的 SQL 如下：
     * <p>
     * : ==>  Preparing: SELECT RES.* from ACT_RE_PROCDEF RES order by RES.ID_ asc
     * : ==> Parameters:
     * : <==      Total: 2
     */
    @Test
    void test01() {
        //查询所有定义的流程
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition pd : list) {
            logger.info("id:{},name:{},version:{},category:{}", pd.getId(), pd.getName(), pd.getVersion(), pd.getCategory());
        }
    }
}
