package org.javaboy.rl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.javaboy.rl.annotation.RateLimiter;
import org.javaboy.rl.exception.ServiceException;
import org.javaboy.rl.limit.LimitType;
import org.javaboy.rl.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collections;
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
public class RateLimitAspect {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    RedisScript<Long> redisScript;

    @Before("@annotation(rateLimiter)")
    public void before(JoinPoint jp, RateLimiter rateLimiter) throws ServiceException {
        int count = rateLimiter.count();
        int time = rateLimiter.time();
        //获取限流接口存在 Redis 中的 key
        String combineKey = getCombineKey(rateLimiter, jp);
        List<Object> keys = Collections.singletonList(combineKey);
        try {
            Long number = redisTemplate.execute(redisScript, keys, count, time);
            if (number == null || number.intValue() > count) {
                throw new ServiceException("访问过于频繁，请稍后再试");
            }
            logger.info("限制请求：{},当前请求：{},缓存的 key：{}", count, number.intValue(), combineKey);
        } catch (Exception e) {
//            e.printStackTrace();
            throw e;
        }
    }

    /**
     *  rate_limit:11.11.11.11-org.javaboy.rl.controller.HelloController-hello()
     *  rate_limit:org.javaboy.rl.controller.HelloController-hello()
     * @param rateLimiter
     * @param jp
     * @return
     */
    private String getCombineKey(RateLimiter rateLimiter, JoinPoint jp) {
        StringBuffer key = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            //如果是基于 IP 地址限流，那么 IP 地址要作为限流 key 的一部分
            key.append(IpUtils.getIpAddr(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
            key.append("-");
        }
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        key.append(declaringClass.getName()).append("-")
                .append(method.getName());
        return key.toString();
    }
}
