package org.javaboy.tienchin.contract.service.impl;

import org.javaboy.tienchin.contract.domain.Contract;
import org.javaboy.tienchin.contract.mapper.ContractMapper;
import org.javaboy.tienchin.contract.service.IContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2023-02-22
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

}