package org.javaboy.aware_demo;

import org.javaboy.aware_demo.service.BeanUtils;
import org.javaboy.aware_demo.service.BeanUtils2;
import org.javaboy.aware_demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AwareDemoApplicationTests {

    @Test
    void contextLoads() {
//        UserService userService = BeanUtils.getBean("userService");
        UserService userService = BeanUtils2.getBean("userService");
        userService.sayHello();
    }

}
