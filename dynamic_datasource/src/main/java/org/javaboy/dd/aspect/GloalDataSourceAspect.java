package org.javaboy.dd.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.javaboy.dd.datasource.DataSourceType;
import org.javaboy.dd.datasource.DynamicDataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
//@Aspect
//@Component
//@Order(10)
public class GloalDataSourceAspect {

    @Autowired
    HttpSession session;

    @Pointcut("execution(* org.javaboy.dd.service.*.*(..))")
    public void pc() {

    }

    @Around("pc()")
    public Object around(ProceedingJoinPoint pjp) {
        DynamicDataSourceContextHolder.setDataSourceType((String) session.getAttribute(DataSourceType.DS_SESSION_KEY));
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
        return null;
    }
}
