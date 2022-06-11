package org.javaboy.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import org.aspectj.weaver.ast.Var;
import org.javaboy.account.mapper.AccountMapper;
import org.javaboy.account.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    AccountMapper accountMapper;

    /**
     * 这里实际上就是准备工作，也是分布式事务一阶段的工作
     * 这个时候主要去检查一下账户是否存在，。检查一下余额是否充足，把要扣的钱先冻结起来
     * @param userId
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean prepare(String userId, Double money) {
        Account account = accountMapper.getAccountByUserId(userId);
        if (account == null) {
            throw new RuntimeException("账户不存在");
        }
        if (account.getMoney() < money) {
            throw new RuntimeException("账户余额不足，预扣款失败");
        }
        //先把钱冻结起来
        account.setFreezeMoney(account.getFreezeMoney() + money);
        account.setMoney(account.getMoney() - money);
        Integer i = accountMapper.updateAccount(account);
        logger.info("{} 账户预扣款 {} 元", userId, money);
        return i == 1;
    }

    /**
     * 二阶段 commit 操作
     * 实际扣款阶段
     * @param actionContext
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean commit(BusinessActionContext actionContext) {
        //获取 prepare 阶段的两个参数
        String userId = (String) actionContext.getActionContext("userId");
        double money = ((BigDecimal) actionContext.getActionContext("money")).doubleValue();
        Account account = accountMapper.getAccountByUserId(userId);
        if (account.getFreezeMoney() < money) {
            throw new RuntimeException("账户余额不足，扣款失败");
        }
        account.setFreezeMoney(account.getFreezeMoney() - money);
        Integer i = accountMapper.updateAccount(account);
        logger.info("{} 账户扣款 {} 元", userId, money);
        return i == 1;
    }

    /**
     * 二阶段回滚
     *
     * 主要是把冻结的钱释放
     * @param actionContext
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean rollback(BusinessActionContext actionContext) {
        //获取 prepare 阶段的两个参数
        String userId = (String) actionContext.getActionContext("userId");
        double money = ((BigDecimal) actionContext.getActionContext("money")).doubleValue();
        Account account = accountMapper.getAccountByUserId(userId);
        if (account.getFreezeMoney() >= money) {
            account.setMoney(account.getMoney() + money);
            account.setFreezeMoney(account.getFreezeMoney() - money);
            Integer i = accountMapper.updateAccount(account);
            logger.info("{} 账户释放冻结金额 {} 元", userId, money);
            return i == 1;
        }
        logger.info("{} 账户冻结金额已释放", userId);
        return true;
    }
}
