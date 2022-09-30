package org.javaboy.flowableidm;

import org.flowable.common.engine.api.management.TableMetaData;
import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.IdmManagementService;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class FlowableIdmApplicationTests {

    /**
     * IdentityService 专门负责和用户相关的操作，例如添加/删除/修改用户或者添加/删除/修改用户组等
     */
    @Autowired
    IdentityService identityService;
    @Autowired
    IdmManagementService idmManagementService;
    private static final Logger logger = LoggerFactory.getLogger(FlowableIdmApplicationTests.class);


    /**
     *
     *
     *
     *
     */
    @Test
    void test15() {
        //查询系统信息，本质上是查询 ACT_ID_PROPERTY
        Map<String, String> properties = idmManagementService.getProperties();
        Set<String> key = properties.keySet();
        for (String s : key) {
            logger.info("key:{};value:{}", s, properties.get(s));
        }
        //查询实体类所对应的表名称
        String groupTableName = idmManagementService.getTableName(Group.class);
        logger.info("tableName:{}", groupTableName);
        //获取表的相信信息
        TableMetaData tableMetaData = idmManagementService.getTableMetaData(groupTableName);
        logger.info("列名：{}",tableMetaData.getColumnNames());
        logger.info("列的类型：{}",tableMetaData.getColumnTypes());
        logger.info("表名：{}",tableMetaData.getTableName());
    }

    /**
     * 自定义查询 SQL
     */
    @Test
    void test14() {
        List<Group> list = identityService.createNativeGroupQuery().sql("SELECT RES.* from ACT_ID_GROUP RES WHERE exists(select 1 from ACT_ID_MEMBERSHIP M where M.GROUP_ID_ = RES.ID_ and M.USER_ID_ = #{userId}) order by RES.ID_ asc").parameter("userId", "javaboy").list();
        for (Group group : list) {
            logger.info("id:{},name:{}",group.getId(),group.getName());
        }
    }

    /**
     * 按照用户组中的用户去查询，这个需要多表联合查询，下面案例，查询包含 javaboy 这个用户的用户组
     *
     * 对应的 SQL：
     *
     * : ==>  Preparing: SELECT RES.* from ACT_ID_GROUP RES WHERE exists(select 1 from ACT_ID_MEMBERSHIP M where M.GROUP_ID_ = RES.ID_ and M.USER_ID_ = ?) order by RES.ID_ asc
     * : ==> Parameters: javaboy(String)
     * : <==      Total: 1
     */
    @Test
    void test13() {
        List<Group> list = identityService.createGroupQuery().groupMember("javaboy").list();
        for (Group group : list) {
            logger.info("id:{},name:{}",group.getId(),group.getName());
        }
    }

    /**
     * 查询用户组：根据用户组名称去查询，注意的是，用户组名称不唯一
     *
     * 对应的 SQL 如下：
     *
     * : ==>  Preparing: SELECT RES.* from ACT_ID_GROUP RES WHERE RES.NAME_ = ? order by RES.ID_ asc
     * : ==> Parameters: CEO(String)
     * : <==      Total: 1
     */
    @Test
    void test12() {
        List<Group> list = identityService.createGroupQuery().groupName("CEO").list();
        for (Group group : list) {
            logger.info("id:{},name:{}",group.getId(),group.getName());
        }

    }

    /**
     * 修改用户组：将 managers 这个用户组的 name 改为 CEO
     *
     * 跟之前 user 的更新一样，更新之前先查询，否则 revision 可能不对，会导致更新失败
     *
     * : ==>  Preparing: update ACT_ID_GROUP SET REV_ = ?, NAME_ = ?, TYPE_ = ? where ID_ = ? and REV_ = ?
     * : ==> Parameters: 2(Integer), CEO(String), null, managers(String), 1(Integer)
     * : <==    Updates: 1
     *
     * 从这个 SQL 中，我们可以看出来乐观锁的具体使用方式，先查询出来一个 Group，revision 为 1，然后更新的时候，将 revision 设置为 2，但是在更新条件中，revision 还是使用 1，这样当我们更新的时候，就可以确保在我们更新之前，这条记录没有被人更新过（如果被人更新过，则这条记录的 revision 就为 2 了，本次更新就会失败）
     */
    @Test
    void test11() {
        //注意，更新之前先查询（因为这个表中使用了乐观锁）
        Group g = identityService.createGroupQuery().groupId("managers").singleResult();
        g.setName("CEO");
        identityService.saveGroup(g);
    }

    /**
     * 给用户组添加用户
     *
     * 对应的 SQL 语句
     *
     * : ==>  Preparing: insert into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) values ( ?, ? )
     * : ==> Parameters: javaboy(String), managers(String)
     * : <==    Updates: 1
     */
    @Test
    void test10() {
        String groupId = "managers";
        String userId = "javaboy";
        //添加用户和用户组之间的关联关系
        //注意，表的底层使用了外键，所以这里需要确保传递的参数都是真是存在的
        identityService.createMembership(userId,groupId);
    }


    /**
     * 根据 ID 删除一个 Group
     *
     * 对应的 SQL：
     *
     * : ==>  Preparing: delete from ACT_ID_MEMBERSHIP where GROUP_ID_ = ?
     * : ==> Parameters: leader(String)
     * : <==    Updates: 0
     * : ==>  Preparing: delete from ACT_ID_GROUP where ID_ = ? and REV_ = ?
     * : ==> Parameters: leader(String), 1(Integer)
     * : <==    Updates: 1
     *
     *
     * 为什么有两个删除 SQL？
     *
     * ACT_ID_MEMBERSHIP 表中保存的是用户 ID 和 组 ID 之间的关联关系，所以，当删除一个用户组的时候，需要先删除组中的用户，第一个删除语句其实就是干这个事情。
     *
     * 第二个删除语句就是删除具体的用户组
     *
     */
    @Test
    void test09() {
        identityService.deleteGroup("leader");
    }

    /**
     * 用户组操作的表是 ACT_ID_GROUP
     *
     * 具体执行的 SQL
     *
     *: inserting: org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl@37e0614e
     * : ==>  Preparing: insert into ACT_ID_GROUP (ID_, REV_, NAME_, TYPE_) values ( ?, 1, ?, ? )
     * : ==> Parameters: managers(String), 经理(String), null
     * : <==    Updates: 1
     */
    @Test
    void test08() {
        //添加用户组
        GroupEntityImpl g = new GroupEntityImpl();
        g.setName("经理");
        g.setId("managers");
        //和用户一样，组的信息中也使用了乐观锁，所以记得要加 revision
        g.setRevision(0);
        identityService.saveGroup(g);
    }

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
