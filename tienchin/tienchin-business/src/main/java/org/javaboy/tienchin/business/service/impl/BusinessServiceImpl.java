package org.javaboy.tienchin.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.javaboy.tienchin.assignment.domain.Assignment;
import org.javaboy.tienchin.assignment.service.IAssignmentService;
import org.javaboy.tienchin.business.domain.Business;
import org.javaboy.tienchin.business.domain.vo.BusinessSummary;
import org.javaboy.tienchin.business.mapper.BusinessMapper;
import org.javaboy.tienchin.business.service.IBusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.common.constant.TienChinConstants;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 线索 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2023-02-04
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements IBusinessService {

    @Autowired
    BusinessMapper businessMapper;

    @Autowired
    IAssignmentService assignmentService;

    @Override
    public List<BusinessSummary> selectBusinessList() {
        return businessMapper.selectBusinessList();
    }

    @Override
    @Transactional
    public AjaxResult addBusiness(Business business) {
        QueryWrapper<Business> qw = new QueryWrapper<>();
        //按照手机号码查询这个客户，如果这个客户的信息已经录入了，则停止录入
        qw.lambda().eq(Business::getPhone, business.getPhone());
        Business one = getOne(qw);
        if (one != null) {
            return AjaxResult.error("客户手机号码重复，录入失败");
        }
        try {
            business.setStatus(TienChinConstants.BUSINESS_ALLOCATED);
            business.setCreateTime(LocalDateTime.now());
            business.setCreateBy(SecurityUtils.getUsername());
            business.setNextTime(LocalDateTime.now().plusHours(TienChinConstants.NEXT_FOLLOW_TIME));
            save(business);
            Assignment assignment = new Assignment();
            assignment.setAssignId(business.getBusinessId());
            assignment.setLatest(true);
            assignment.setType(TienChinConstants.BUSINESS_TYPE);
            assignment.setDeptId(SecurityUtils.getDeptId());
            assignment.setCreateBy(SecurityUtils.getUsername());
            assignment.setUserId(SecurityUtils.getUserId());
            assignment.setCreateTime(LocalDateTime.now());
            assignment.setUserName(SecurityUtils.getUsername());
            assignmentService.save(assignment);
            return AjaxResult.success("商机录入成功");
        } catch (Exception e) {
            return AjaxResult.error("商机录入失败：" + e.getMessage());
        }
    }
}
