package org.javaboy.flowableprocess;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
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
public class ReceiveTaskTest {

    @Autowired
    RuntimeService runtimeService;
    private static final Logger logger = LoggerFactory.getLogger(ReceiveTaskTest.class);

    @Test
    void test02() {
        List<Execution> list = runtimeService.createExecutionQuery().activityId("sid-7BF722A2-D677-4C18-B745-40F695F788CF").list();
        for (Execution execution : list) {
            //触发一个 ReceiveTask 继续向下执行，但是这里需要的参数是一个
            runtimeService.trigger(execution.getId());
        }
    }

    @Test
    void test01() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKeyAndTenantId("ReceiveTaskDemo", "javaboy");
        logger.info("id:{},name:{}", pi.getId(), pi.getName());
    }
}
