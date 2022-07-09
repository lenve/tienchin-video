package org.javaboy.proxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
public class ProxyDemo02 {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dog.class);
        enhancer.setCallback(new DogInterceptor());
        //这里拿到的 dog 对象，实际上不是我们自己定义的 Dog 对象，而是通过动态代理为 Dog 类自动生成的子类的对象
        Dog dog = (Dog) enhancer.create();
        dog.eat();
    }
}
