package org.javaboy.dd.service;

import org.javaboy.dd.annotation.DataSource;
import org.javaboy.dd.mapper.SlaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Service
public class SlaveService {

    @Autowired
    SlaveMapper slaveMapper;
    @DataSource("slave")
    public void updateUserAge(String username, Integer age) {
        slaveMapper.updateUserAge(username, age);
        int i = 1 / 0;
    }
}
