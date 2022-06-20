package org.javaboy.order.service;

import org.javaboy.common.RespBean;
import org.javaboy.order.feign.AccountFeign;
import org.javaboy.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Service
public class OrderService {
    @Autowired
    AccountFeign accountFeign;

    @Autowired
    OrderMapper orderMapper;

    public boolean createOrder(String account, String productId, Integer count) {
        //先去扣款，假设每个产品100块钱
        RespBean respBean = accountFeign.deduct(account, count * 100.0);
        int i = orderMapper.addOrder(account, productId, count, count * 100.0);
        return i == 1 && respBean.getStatus() == 200;
    }
}
