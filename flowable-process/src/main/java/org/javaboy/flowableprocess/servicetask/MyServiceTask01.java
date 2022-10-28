package org.javaboy.flowableprocess.servicetask;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

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
 * 这是我们自定义的监听器类，这个类也就是 ServiceTask 执行到这里的时候，会自动执行该类中的 execute 方法
 */
public class MyServiceTask01 implements JavaDelegate {

    Expression username;

    @Override
    public void execute(DelegateExecution execution) {
        //获取 username 的值
        System.out.println("username.getExpressionText() = " + username.getExpressionText());
        System.out.println("username.getValue(execution) = " + username.getValue(execution));
        System.out.println("=============MyServiceTask01=============");
    }
}
