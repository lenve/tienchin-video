package org.javaboy.flowableprocess;

import org.flowable.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
public class ScriptTaskTest {
    @Autowired
    RuntimeService runtimeService;
    @Test
    void test01() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("a", 99);
        vars.put("b", 98);
        runtimeService.startProcessInstanceByKey("ScriptTaskDemo01", vars);
    }
}
