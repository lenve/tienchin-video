package org.javaboy.rl.limit;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
public enum LimitType {
    /**
     * 默认的限流，即针对当前接口的限流
     */

     DEFAULT,

     /**
     * 针对某一个 IP 地址的限流
     */
    IP
}
