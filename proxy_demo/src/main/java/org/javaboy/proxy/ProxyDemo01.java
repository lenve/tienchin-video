package org.javaboy.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
public class ProxyDemo01 {
    public static void main(String[] args) {
        CalculatorImpl calculatorImpl = new CalculatorImpl();
        /**
         * 1. classLoader
         * 2. 将来生成的代理对象实现的接口/被代理的对象实现的接口
         *
         * Proxy.newProxyInstance 方法的返回值其实就是生成的代理对象，这个代理对象是系统自动为 Calculator 接口提供的一个实现类，这个实现类就是（com.sun.proxy.$Proxy0），相当于系统中，现在 Calculator 接口有两个实现类，一个是自动生成的，一个是 CalculatorImpl。
         *
         * 我们最终实际上调用的是自动生成的代理对象中的 add/minus 方法。
         *
         * 自动生成的代理对象，逻辑类似下面这样：
         *
         * public class $Proxy0 implements Calculator{
         *
         *     public int add(int a,int b){
         *         long startTime = System.nanoTime();
         *         //执行反射对象中的方法
         *         Object invoke = method.invoke(calculatorImpl, args);
         *         long endTime = System.nanoTime();
         *         System.out.println(method.getName() + " 方法执行耗时 " + (endTime - startTime) + " 纳秒");
         *         return invoke;
         *     }
         * }
         *
         */
        Calculator calculator = (Calculator) Proxy.newProxyInstance(ProxyDemo01.class.getClassLoader(), new Class[]{Calculator.class}, new InvocationHandler() {
            /**
             * @param proxy 被代理的对象
             * @param method 接口中的方法（add/minus）
             * @param args 方法的参数
             * @return 方法的返回值
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                long startTime = System.nanoTime();
                Object invoke = method.invoke(calculatorImpl, args);
                long endTime = System.nanoTime();
                System.out.println(method.getName() + " 方法执行耗时 " + (endTime - startTime) + " 纳秒");
                return invoke;
            }
        });
        calculator.add(3, 4);
    }
}
