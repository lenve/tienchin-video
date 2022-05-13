package org.javaboy.dd.annotation;

import org.javaboy.dd.datasource.DataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */

/**
 * 这个注解将来可以加在某一个 service 类上或者方法上，通过 value 属性来指定类或者方法应该使用哪个数据源
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DataSource {
    /**
     * 如果一个方法上加了 @DataSource 注解，但是却没有指定数据源的名称，那么默认使用 master 数据源
     * @return
     */
    String value() default DataSourceType.DEFAULT_DS_NAME;
}
