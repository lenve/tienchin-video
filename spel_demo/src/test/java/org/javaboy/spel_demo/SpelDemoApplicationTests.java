package org.javaboy.spel_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

@SpringBootTest
class SpelDemoApplicationTests {

    @Autowired
    BeanFactory beanFactory;

    @Test
    void test05() {
        String exp = "@bs.sayBook()";
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp);
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setBeanResolver(new BeanFactoryResolver(beanFactory));
        String value = expression.getValue(ctx, String.class);
        System.out.println("value = " + value);
    }

    @Test
    void test04() {
        //这个表达式的含义表示调用 user 对象中的 username 属性
        String exp = "#us.sayHello(\"javaboy\")";
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp);
        //创建上下文环境并设置值
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        UserService us = new UserService();
//        ctx.setRootObject(us);
        ctx.setVariable("us", us);
        String value = expression.getValue(ctx, String.class);
        System.out.println("value = " + value);
    }

    @Test
    void test03() {
        //这个表达式的含义表示调用 user 对象中的 username 属性
        String exp = "sayHello()";
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp);
        //创建上下文环境并设置值
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setRootObject(new UserService());
        String value = expression.getValue(ctx, String.class);
        System.out.println("value = " + value);
    }

    @Test
    void test02() {
        //这个表达式的含义表示调用 user 对象中的 username 属性
        String exp = "username";
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp);
        User user = new User();
        user.setId(99);
        user.setUsername("javaboy");
        user.setAddress("广州");
        //创建上下文环境并设置值
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setRootObject(user);
        String value = expression.getValue(ctx, String.class);
        System.out.println("value = " + value);
    }

    @Test
    void test() {
        //这个表达式的含义表示调用 user 对象中的 username 属性
        String exp = "#user.username";
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp);
        User user = new User();
        user.setId(99);
        user.setUsername("javaboy");
        user.setAddress("广州");
        //创建上下文环境并设置值
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("user", user);
        String value = expression.getValue(ctx, String.class);
        System.out.println("value = " + value);
    }

    @Test
    void contextLoads() {
        //eval
        String exp1 = "1+2";
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(exp1);
        Object value = expression.getValue();
        System.out.println("value = " + value);
    }

}
