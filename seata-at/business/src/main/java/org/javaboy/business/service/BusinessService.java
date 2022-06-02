package org.javaboy.business.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.javaboy.business.feign.OrderFeign;
import org.javaboy.business.feign.StorageFeign;
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
public class BusinessService {
    @Autowired
    StorageFeign storageFeign;
    @Autowired
    OrderFeign orderFeign;

    @GlobalTransactional
    public void purchase(String account, String productId, Integer count) {
        orderFeign.createOrder(account, productId, count);
        storageFeign.deduct(productId, count);
    }
}