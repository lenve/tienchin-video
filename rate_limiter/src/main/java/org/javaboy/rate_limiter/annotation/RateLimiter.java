package org.javaboy.rate_limiter.annotation;

import org.javaboy.rate_limiter.enums.LimitType;

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
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiter {
    /**
     * 限流的 key，主要是指前缀
     * @return
     */
    String key() default "rate_limit:";

    /**
     * 限流时间窗
     * @return
     */
    int time() default 60;

    /**
     * 在时间窗内的限流次数
     * @return
     */
    int count() default 100;

    /**
     * 限流类型
     * @return
     */
    LimitType limitType() default LimitType.DEFAULT;

}
