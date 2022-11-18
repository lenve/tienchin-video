package org.javaboy.flowableprocess;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.FormService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.FormType;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.impl.form.DateFormType;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormInfo;
import org.flowable.form.model.FormField;
import org.flowable.form.model.SimpleFormModel;
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
public class FormTest {

    @Autowired
    FormService formService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(FormTest.class);

    @Test
    void test13() {
        Task task = taskService.createTaskQuery().singleResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("days", "100");
        vars.put("reason", "我想休息一百天");
        vars.put("startTime", "2022-11-11");
        vars.put("endTime", "2023-11-21");
        taskService.complete(task.getId(),vars);
    }

    /**
     * 先去查看某一个 UserTask 中需要填写的表单内容
     */
    @Test
    void test12() {
        Task task = taskService.createTaskQuery().singleResult();
        //获取 UserTask 上的表单信息
        FormInfo formInfo = taskService.getTaskFormModel(task.getId());
        String key = formInfo.getKey();
        String id = formInfo.getId();
        String name = formInfo.getName();
        String description = formInfo.getDescription();
        int version = formInfo.getVersion();
        logger.info("key:{},id:{},name:{},description:{},version:{}", key, id, name, description, version);
        SimpleFormModel formModel = (SimpleFormModel) formInfo.getFormModel();
        //获取表单中的各个字段
        List<FormField> fields = formModel.getFields();
        for (FormField field : fields) {
            String type = field.getType();
            Object value = field.getValue();
            String name1 = field.getName();
            String id1 = field.getId();
            logger.info("表单上的字段：type:{},value:{},name:{},id:{}", type, value, name1, id1);
        }
    }

    /**
     * 启动一个流程实例
     */
    @Test
    void test11() {
        runtimeService.startProcessInstanceByKey("JsonFormatForm");
    }

    /**
     * 接下来想要进行流程审批
     */
    @Test
    void test10() {
        Task task = taskService.createTaskQuery().singleResult();
        Map<String, String> vars = new HashMap<>();
        vars.put("days", "100");
        formService.submitTaskFormData(task.getId(),vars);
    }


    /**
     * 接下来，进入到组长审批环节
     * 在组长审批之前，组长审批这个 UserTask 上，其实也有一个外置表单，那么在审批之前，想要查看一下这个外置表单的数据
     */
    @Test
    void test09() {
        Task task = taskService.createTaskQuery().singleResult();
        //获取渲染之后的外置表单，这里会自动的读取流程变量，并将表单中的值给渲染出来
        String renderedTaskForm = (String) formService.getRenderedTaskForm(task.getId());
        logger.info("renderedTaskForm:{}", renderedTaskForm);
    }


    /**
     * 启动一个带有外置表单的流程
     */
    @Test
    void test08() {
        //查询流程定义的信息
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("ExtFormDemo01").latestVersion().singleResult();
        Map<String, String> vars = new HashMap<>();
        vars.put("days", "10");
        vars.put("reason", "休息一下");
        vars.put("startTime", "2022-11-11");
        vars.put("endTime", "2022-11-21");
        ProcessInstance pi = formService.submitStartFormData(pd.getId(), vars);
    }

    /**
     * 在流程启动之前，先去查看一下启动节点上定义的外置表单的属性以及内容
     */
    @Test
    void test07() {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("ExtFormDemo01").latestVersion().singleResult();
        //查询启动节点上，外置表单的 key
        String startFormKey = formService.getStartFormKey(pd.getId());
        logger.info("startFormKey:{}", startFormKey);
        //查询启动节点上，渲染之后的流程表单，这个方法主要是针对外置表单来使用的，动态表单是不支持这个方法
        String renderedStartForm = (String) formService.getRenderedStartForm(pd.getId());
        logger.info("renderedStartForm:{}", renderedStartForm);
    }

    /**
     * taskService.complete(); 方法当然也可以用来完成表单数据，但是这个方法在完成表单的时候，不会进行表单数据的校验
     */
    @Test
    void test06() {
        Task task = taskService.createTaskQuery().singleResult();
        Map<String, String> vars = new HashMap<>();
        vars.put("days", "100");
        vars.put("reason", "我想休息一百天");
        vars.put("startTime", "2022-11-11");
        vars.put("endTime", "2023-11-21");
        vars.put("type", "aksforleave");
        //这种完成方式，对于传递的表单数据会进行校验，但是日期的格式不会校验
        formService.submitTaskFormData(task.getId(),vars);
    }

    /**
     * 修改某一个 UserTask 中的表单数据
     */
    @Test
    void test05() {
        Task task = taskService.createTaskQuery().singleResult();
        //保存某一个 UserTask 的表单数据
        Map<String, String> vars = new HashMap<>();
        //虽然这里看起来是按照变量的方式处理的，但是实际上会进行表单的各种属性检查
        vars.put("days", "200");
        formService.saveFormData(task.getId(),vars);
    }

    /**
     * 查询某一个任务节点的表单信息，每一个任务节点表单上的数据，都是通过前面的节点传递过来的，前面节点有的数据，每一个任务节点才有，前面节点没有数据，每一个任务节点就没有
     *
     * 例如 startEvent 的表单上有 days、reason、startTime、endTime 四个字段，当时当前的 UserTask 上的表单只有 days 和 type 两个字段，那么 days 的值，就是 startEvent 当时填入的值，type 则没有值
     *
     */
    @Test
    void test04() {
        Task task = taskService.createTaskQuery().singleResult();
        //查询某一个 Task 上的表单数据
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        //获取动态表单上的各种属性列表并遍历
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        for (FormProperty fp : formProperties) {
            FormType type = fp.getType();
            //这个是获取类型的名字
            String typeName = type.getName();
            Object info = "";
            //对于日前和枚举类型，还可以获取该字段的额外信息
            if (type instanceof EnumFormType) {
                //如果当前字段是一个枚举类型，获取枚举类型的值
                info = type.getInformation("values");
            } else if (type instanceof DateFormType) {
                info = type.getInformation("datePattern");
            }
            String id = fp.getId();
            String name = fp.getName();
            String value = fp.getValue();
            logger.info("id:{},name:{},value:{},type:{},info:{}", id, name, value, typeName, info);
        }
    }

    @Test
    void test03() {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("DynamicFormDemo01").latestVersion().singleResult();
        Authentication.setAuthenticatedUserId("wangwu");
        Map<String, String> vars = new HashMap<>();
        vars.put("days", "100");
        vars.put("reason", "我想休息一百天");
        vars.put("startTime", "2022-11-11");
        vars.put("endTime", "2023-11-21");
        vars.put("type", "aksforleave");
        //用这种方式去提交，会检查到各个表单属性是否都是正确的
        ProcessInstance pi = formService.submitStartFormData(pd.getId(), vars);
    }

    @Test
    void test02() {
        Authentication.setAuthenticatedUserId("wangwu");
        Map<String, Object> vars = new HashMap<>();
        vars.put("days", 10);
        vars.put("reason", "我想休息几天");
        vars.put("startTime", "2022-11-11");
        vars.put("endTime", "2022-11-21");
        vars.put("type", "aa");
        runtimeService.startProcessInstanceByKey("DynamicFormDemo01", vars);
    }

    /**
     * 查询一下动态表单的定义信息
     * <p>
     * 例如，当我们启动一个流程实例的时候，如果使用了表单，我们就可以通过接下来的查询，知道这个流程需要传递哪些参数，以及这些参数的类型
     * <p>
     * 所以，接下来的这个查询，是在表单定义的基础上进行查询的，而不是在流程的基础上查询的
     */
    @Test
    void test01() {
        //查询到 DynamicFormDemo01 的表单定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("DynamicFormDemo01").latestVersion().singleResult();
        //获取启动节点上的表单定义信息
        StartFormData startFormData = formService.getStartFormData(pd.getId());
        logger.info("流程部署的 id：{}，表单的 key（这里没有表单的 key，因为这个属性实际上是给外置表单使用的）：{}", pd.getId(), startFormData.getFormKey());
        //这个是获取 startEvent 上面的所有的动态表单属性
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty fp : formProperties) {
            FormType type = fp.getType();
            //这个是获取类型的名字
            String typeName = type.getName();
            Object info = "";
            //对于日前和枚举类型，还可以获取该字段的额外信息
            if (type instanceof EnumFormType) {
                //如果当前字段是一个枚举类型，获取枚举类型的值
                info = type.getInformation("values");
            } else if (type instanceof DateFormType) {
                info = type.getInformation("datePattern");
            }
            String id = fp.getId();
            String name = fp.getName();
            String value = fp.getValue();
            logger.info("id:{},name:{},value:{},type:{},info:{}", id, name, value, typeName, info);
        }
    }
}
