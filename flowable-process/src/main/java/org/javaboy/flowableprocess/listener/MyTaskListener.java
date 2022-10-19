package org.javaboy.flowableprocess.listener;

import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 *
 *
 * 这就是一个任务监听器
 */
public class MyTaskListener implements TaskListener {
    /**
     * 当任务被创建的时候，这个方法会被触发
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        //可以在这里设置任务的处理人
        delegateTask.setAssignee("wangwu");
    }
}
