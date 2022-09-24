package org.javaboy.flowableidm;

import org.flowable.engine.IdentityService;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FlowableIdmApplicationTests {

    /**
     * IdentityService 专门负责和用户相关的操作，例如添加/删除/修改用户或者添加/删除/修改用户组等
     */
    @Autowired
    IdentityService identityService;
    private static final Logger logger = LoggerFactory.getLogger(FlowableIdmApplicationTests.class);

    @Test
    void test07() {
        List<User> list = identityService.createNativeUserQuery().sql("select * from ACT_ID_USER where EMAIL_=#{email}").parameter("email", "javaboy@qq.com").list();
        for (User user : list) {
            logger.info("id:{};displayName:{}", user.getId(), user.getDisplayName());
        }
    }

    @Test
    void test06() {
        //根据 id 排序，并查询所有用户
        List<User> list = identityService.createUserQuery().orderByUserId().desc().list();
        for (User user : list) {
            logger.info("id:{};displayName:{}", user.getId(), user.getDisplayName());
        }
    }

    @Test
    void test05() {
        List<User> list = identityService.createUserQuery().userDisplayNameLike("%zhang%").list();
        for (User user : list) {
            logger.info("id:{};displayName:{}", user.getId(), user.getDisplayName());
        }
    }

    @Test
    void test04() {
        identityService.deleteUser("javaboy");
    }

    @Test
    void test03() {
        User user = identityService.createUserQuery().userId("javaboy").singleResult();
        user.setEmail("33@33.com");
        user.setPassword("888");
        //修改用户密码需要调用 updateUserPassword 方法，但是实际上该方法也能修改用户的其他属性
        identityService.updateUserPassword(user);
    }

    @Test
    void test02() {
        User user = identityService.createUserQuery().userId("javaboy").singleResult();
        user.setEmail("221@22.com");
        identityService.saveUser(user);
    }

    /**
     * 更新用户信息
     * <p>
     * saveUser 方法可以用来更新用户信息，但是不能用来更新密码。
     * 每更新一次，数据库的 reversion 会自增 1
     */
    @Test
    void test01() {
        UserEntityImpl user = new UserEntityImpl();
        user.setId("javaboy");
        user.setEmail("11@11.com");
        //注意，修改的时候，需要确保 reversion 的版本号和数据库中的版本号保持一致
        user.setRevision(2);
        identityService.saveUser(user);
    }

    @Test
    void contextLoads() {
        //创建一个用户对象
        UserEntityImpl user = new UserEntityImpl();
        user.setId("javaboy");
        user.setDisplayName("江南一点雨");
        user.setPassword("123");
        user.setFirstName("javaboy");
        user.setLastName("javaboy");
        user.setEmail("javaboy@qq.com");
        //注意，如果是添加用户，一定要记得设置这个 Revision 属性为 0，表示当前用户是一个新的用户，而不是更新的用户
        //flowable 的用户表使用了乐观锁，而 Revision 字段其实就是配合乐观锁使用的
        user.setRevision(0);
        //保存一个用户
        //这里是有两方面的功能：1. 如果用户已经存在，则更新；2. 如果用户不存在，则添加
        identityService.saveUser(user);
    }

}
