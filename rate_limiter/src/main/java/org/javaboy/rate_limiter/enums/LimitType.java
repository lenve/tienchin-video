package org.javaboy.rate_limiter.enums;

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
 * 限流的类型
 */
public enum LimitType {

    /**
     * 默认的限流策略，针对某一个接口进行限流
     */
    DEFAULT,
    /**
     * 针对某一个 IP 进行限流
     */
    IP
}
