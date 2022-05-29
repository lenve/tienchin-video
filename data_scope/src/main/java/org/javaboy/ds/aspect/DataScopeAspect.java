package org.javaboy.ds.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.javaboy.ds.annoataion.DataScope;
import org.javaboy.ds.entity.BaseEntity;
import org.javaboy.ds.entity.Role;
import org.javaboy.ds.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
@Aspect
@Component
public class DataScopeAspect {
    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_CUSTOM = "2";
    public static final String DATA_SCOPE_DEPT = "3";
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";
    public static final String DATA_SCOPE_SELF = "5";
    public static final String DATA_SCOPE = "data_scope";

    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint jp, DataScope dataScope) {
        clearDataScope(jp);
        //获取当前登录用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserId() == 1L) {
            //说明是超级管理员，不用进行权限过滤
            return;
        }
        StringBuilder sql = new StringBuilder();
        List<Role> roles = user.getRoles();
        //select * from sys_dept d where d.del_flag='0' and (xxx OR xxx OR xxx)
        //d.dept_id in(select rd.dept_id from sys_user_role ur,sys_role_dept rd where ur.user_id=2 and ur.role_id=rd.role_id) 代表一个 xxx
        for (Role role : roles) {
            //获取角色对应的数据权限
            String ds = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(ds)) {
                //如果用户能够查看所有数据，这里什么都用不做
                return;
            } else if (DATA_SCOPE_CUSTOM.equals(ds)) {
                //自定义的数据权限，那么就根据 用户角色去查找到部门 id
                sql.append(String.format(" OR %s.dept_id in(select rd.dept_id from sys_role_dept rd where rd.role_id=%d)", dataScope.deptAlias(), role.getRoleId()));
            } else if (DATA_SCOPE_DEPT.equals(ds)) {
                sql.append(String.format(" OR %s.dept_id=%d", dataScope.deptAlias(), user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(ds)) {
                sql.append(String.format(" OR %s.dept_id in(select dept_id from sys_dept where dept_id=%d or find_in_set(%d,`ancestors`))", dataScope.deptAlias(), user.getDeptId(), user.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(ds)) {
                String s = dataScope.userAlias();
                if ("".equals(s)) {
                    //数据权限仅限于本人
                    sql.append(" OR 1=0");
                } else {
                    sql.append(String.format(" OR %s.user_id=%d", dataScope.userAlias(), user.getUserId()));
                }
            }
        }
        // and( xxx or xxx or xxx)
        Object arg = jp.getArgs()[0];
        if (arg != null && arg instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) arg;
            baseEntity.getParams().put(DATA_SCOPE, " AND ("+sql.substring(4)+")");
        }
    }

    /**
     * 如果 params 中已经有参数了，则删除掉，防止 SQL 注入
     *
     * @param jp
     */
    private void clearDataScope(JoinPoint jp) {
        Object arg = jp.getArgs()[0];
        if (arg != null && arg instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) arg;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
