package org.javaboy.dd;

import org.javaboy.dd.model.User;
import org.javaboy.dd.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DynamicDatasourceApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void test01() {
        userService.test();
    }


    @Test
    void contextLoads() {
        List<User> list = userService.getAllUsers();
        for (User user : list) {
            System.out.println(user);
        }
    }

}
