package org.javaboy.rate_limiter.controller;

import org.javaboy.rate_limiter.annotation.RateLimiter;
import org.javaboy.rate_limiter.enums.LimitType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    /**
     * 限流，10 秒之内，这个接口可以访问 3 次
     */
    @RateLimiter(time = 10, count = 3,limitType = LimitType.IP)
    public String hello() {
        return "hello";
    }
}
