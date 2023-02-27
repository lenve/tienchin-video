package org.javaboy.tienchin.web.controller.tienchin;

import org.javaboy.tienchin.business.service.IBusinessService;
import org.javaboy.tienchin.common.core.controller.BaseController;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.core.page.TableDataInfo;
import org.javaboy.tienchin.contract.domain.Contract;
import org.javaboy.tienchin.contract.domain.vo.ContractSummary;
import org.javaboy.tienchin.contract.service.IContractService;
import org.javaboy.tienchin.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2023-02-22
 */
@RestController
@RequestMapping("/tienchin/contract")
public class ContractController extends BaseController {

    @Autowired
    IContractService contractService;

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    IBusinessService businessService;

    @PreAuthorize("hasPermission('tienchin:contract:create')")
    @PostMapping("/upload")
    public AjaxResult uploadContractFile(HttpServletRequest req,MultipartFile file) {
        return contractService.uploadContractFile(req,file);
    }

    @PreAuthorize("hasPermission('tienchin:contract:create')")
    @DeleteMapping("/{year}/{month}/{day}/{name}")
    public AjaxResult deleteContractFile(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String name) {
        return contractService.deleteContractFile(year, month, day, name);
    }

    @PreAuthorize("hasPermission('tienchin:contract:create')")
    @PostMapping
    public AjaxResult addContract(@RequestBody Contract contract) {
        return contractService.addContract(contract);
    }

    @GetMapping("/users/{deptId}")
    @PreAuthorize("hasPermission('tienchin:contract:create')")
    public AjaxResult getUsersByDeptId(@PathVariable Long deptId) {
        return sysUserService.getUsersByDeptId(deptId);
    }

    @GetMapping("/customer/{phone}")
    @PreAuthorize("hasPermission('tienchin:contract:create')")
    public AjaxResult geContractInfoPhone(@PathVariable String phone) {
        return contractService.geContractInfoPhone(phone);
    }

    /**
     * 查询所有待审批的合同
     * @return
     */
    @PreAuthorize("hasPermission('tienchin:contract:list')")
    @GetMapping("/unapprove")
    public TableDataInfo getUnapproveTask() {
        startPage();
        List<ContractSummary> list = contractService.getUnapproveTask();
        return getDataTable(list);
    }

    @PreAuthorize("hasPermission('tienchin:contract:view')")
    @GetMapping("/views/{contractId}")
    public AjaxResult getContractById(@PathVariable Integer contractId) {
        return contractService.getContractById(contractId);
    }
}
