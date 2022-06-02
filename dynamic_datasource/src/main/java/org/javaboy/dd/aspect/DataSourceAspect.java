package org.javaboy.dd.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.javaboy.dd.DynamicDatasourceApplication;
import org.javaboy.dd.annotation.DataSource;
import org.javaboy.dd.datasource.DynamicDataSourceContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {

    /**
     * @annotation(org.javaboy.dd.annotation.DataSource) 表示方法上有 @DataSource 注解就将方法拦截下来
     * @within(org.javaboy.dd.annotation.DataSource) 表示如果类上面有 @DataSource 注解，就将类中的方法拦截下来
     */
    @Pointcut("@annotation(org.javaboy.dd.annotation.DataSource) || @within(org.javaboy.dd.annotation.DataSource)")
    public void pc() {

    }

    @Around("pc()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //获取方法上面的有效注解
        DataSource dataSource = getDataSource(pjp);
        if (dataSource != null) {
            //获取注解中数据源的名称
            String value = dataSource.value();
            DynamicDataSourceContextHolder.setDataSourceType(value);
        }
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    private DataSource getDataSource(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //查找方法上的注解
        DataSource annotation = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (annotation != null) {
            //说明方法上面有 @DataSource 注解
            return annotation;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
