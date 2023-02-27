package org.javaboy.tienchin.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.flowable.cmmn.engine.impl.process.ProcessInstanceService;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.javaboy.tienchin.business.domain.Business;
import org.javaboy.tienchin.business.service.IBusinessService;
import org.javaboy.tienchin.common.constant.TienChinConstants;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.core.domain.UploadFileResp;
import org.javaboy.tienchin.common.utils.SecurityUtils;
import org.javaboy.tienchin.contract.domain.Contract;
import org.javaboy.tienchin.contract.domain.vo.ContractInfo;
import org.javaboy.tienchin.contract.domain.vo.ContractSummary;
import org.javaboy.tienchin.contract.mapper.ContractMapper;
import org.javaboy.tienchin.contract.service.IContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.course.domain.Course;
import org.javaboy.tienchin.course.service.ICourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2023-02-22
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {
    @Autowired
    IBusinessService businessService;
    @Autowired
    ICourseService courseService;
    @Autowired
    ContractMapper contractMapper;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Value("${tienchin.contract.file}")
    String contractFolder;

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");

    @Override
    public AjaxResult uploadContractFile(HttpServletRequest req, MultipartFile file) {
        String format = sdf.format(new Date());
        String fileDir = contractFolder + format;
        File dir = new File(fileDir);
        if (!dir.exists()) {
            //如果文件夹不存在，那么就将文件夹创建出来
            dir.mkdirs();
        }
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //新的文件名
        String newName = UUID.randomUUID().toString() + "-" + originalFilename;
        try {
            file.transferTo(new File(dir, newName));
            String url = req.getScheme() + "://"
                    + req.getServerName() + ":"
                    + req.getServerPort()
                    + req.getContextPath()
                    + "/tienchin/contract/views"
                    + format + newName;
            UploadFileResp resp = new UploadFileResp();
            resp.setName(originalFilename);
            resp.setUrl(url);
            return AjaxResult.success(resp);
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
        return AjaxResult.error("文件上传失败");
    }

    @Override
    public AjaxResult deleteContractFile(String year, String month, String day, String name) {
        String fileName = contractFolder + File.separator + year + File.separator + month + File.separator + day + File.separator + name;
        File file = new File(fileName);
        boolean delete = file.delete();
        return delete ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    @Transactional
    @Override
    public AjaxResult addContract(Contract contract) {
        //1. 向合同表中添加数据
        //1.1 查询商机 ID 并设置
        QueryWrapper<Business> qw = new QueryWrapper<>();
        qw.lambda().eq(Business::getPhone, contract.getPhone()).orderByDesc(Business::getCreateTime);
        List<Business> list = businessService.list(qw);
        Integer businessId = list.get(0).getBusinessId();
        contract.setBusinessId(businessId);
        //1.2 查询课程价格并设置
        QueryWrapper<Course> cqw = new QueryWrapper<>();
        cqw.lambda().eq(Course::getCourseId, contract.getCourseId());
        Course c = courseService.getOne(cqw);
        contract.setCoursePrice(c.getPrice());
        //1.3 设置通用属性
        contract.setCreateBy(SecurityUtils.getUsername());
        contract.setCreateTime(LocalDateTime.now());
        contract.setDelFlag(0);
        contract.setStatus(TienChinConstants.CONTRACT_UNAPPROVE);
        save(contract);
        //2。 启动流程
        //启动流程
        Map<String, Object> pivars = new HashMap<>();
        pivars.put("currentUser", SecurityUtils.getUsername());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(TienChinConstants.CONTRACT_PROCESS_DEFINITION_ID, pivars);
        Map<String, Object> vars = new HashMap<>();
        vars.put("contractId", contract.getContractId());
        vars.put("approveUser", contract.getApproveUserName());
        vars.put("approveUserId", contract.getApproveUserId());
        Task task = taskService.createTaskQuery().taskAssignee(SecurityUtils.getUsername()).taskDefinitionKey("submitContractTask").singleResult();
        taskService.complete(task.getId(), vars);
        //3。更新合同信息
        contract.setProcessInstanceId(processInstance.getProcessInstanceId());
        updateById(contract);
        return AjaxResult.success("提交成功");
    }

    @Override
    public AjaxResult geContractInfoPhone(String phone) {
        List<ContractInfo> list = contractMapper.geContractInfoPhone(phone);
        if (list != null && list.size() > 0) {
            ContractInfo contractInfo = list.get(0);
            return AjaxResult.success(contractInfo);
        } else {
            return AjaxResult.error("手机号码输入错误，客户不存在");
        }
    }

    @Override
    public List<ContractSummary> getUnapproveTask() {
        List<ContractSummary> result = new ArrayList<>();
        //查询当前用户需要处理的任务
        List<Task> list = taskService.createTaskQuery().taskAssignee(SecurityUtils.getUsername())
                .processDefinitionKey(TienChinConstants.CONTRACT_PROCESS_DEFINITION_ID)
                .active().orderByTaskCreateTime().desc().list();
        for (Task task : list) {
            String taskId = task.getId();
            Integer contractId = (Integer) taskService.getVariable(task.getId(), "contractId");
            ContractSummary summary = new ContractSummary();
            Contract contract = getById(contractId);
            BeanUtils.copyProperties(contract, summary);
            summary.setTaskId(taskId);
            result.add(summary);
        }
        return result;
    }

    @Override
    public AjaxResult getContractById(Integer contractId) {
        Contract contract = getById(contractId);
        return AjaxResult.success(contract);
    }
}