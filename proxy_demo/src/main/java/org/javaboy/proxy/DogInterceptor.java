package org.javaboy.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
public class DogInterceptor implements MethodInterceptor {
    /**
     * 这个方法类似于 InvocationHandler 方法
     *
     * @param o           代理对象
     * @param method      代理的方法
     * @param objects     方法的参数
     * @param methodProxy 方法对象
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        long startTime = System.nanoTime();
        //这个相当于 JDK 动态代理中的 method.invoke 方法
        Object result = methodProxy.invokeSuper(o, objects);
        long endTime = System.nanoTime();
        System.out.println(method.getName() + " 方法执行耗时 " + (endTime - startTime) + " 纳秒");
        return result;
    }
}
