package org.javaboy.account.controller;

import org.javaboy.account.service.AccountService;
import org.javaboy.common.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/deductAccount")
    public RespBean deduct(String account, Double money) {
        if (accountService.deductAccount(account, money)) {
            return RespBean.ok("扣款成功");
        }
        return RespBean.error("扣款失败");
    }
}
