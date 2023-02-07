package org.javaboy.tienchin.web.controller.tienchin.business;

import org.javaboy.tienchin.activity.service.IActivityService;
import org.javaboy.tienchin.business.domain.Business;
import org.javaboy.tienchin.business.domain.vo.BusinessSummary;
import org.javaboy.tienchin.business.service.IBusinessService;
import org.javaboy.tienchin.channel.service.IChannelService;
import org.javaboy.tienchin.clue.domain.Clue;
import org.javaboy.tienchin.clue.domain.vo.ClueSummary;
import org.javaboy.tienchin.clue.domain.vo.ClueVO;
import org.javaboy.tienchin.common.annotation.Log;
import org.javaboy.tienchin.common.core.controller.BaseController;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.core.page.TableDataInfo;
import org.javaboy.tienchin.common.enums.BusinessType;
import org.javaboy.tienchin.common.validator.CreateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tienchin/business")
public class BusinessController extends BaseController {

    @Autowired
    IBusinessService businessService;

    @Autowired
    IChannelService channelService;

    @Autowired
    IActivityService activityService;


    @PreAuthorize("hasPermission('tienchin:clue:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<BusinessSummary> list = businessService.selectBusinessList();
        return getDataTable(list);
    }

    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @Log(title = "商机管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Business business) {
        return businessService.addBusiness(business);
    }

    @PreAuthorize("hasPermission('tienchin:business:create')")
    @GetMapping("/channels")
    public AjaxResult getAllChannels() {
        return AjaxResult.success(channelService.list());
    }

    @PreAuthorize("hasPermission('tienchin:business:create')")
    @GetMapping("/activity/{channelId}")
    public AjaxResult getActivityByChannelId(@PathVariable Integer channelId) {
        return activityService.selectActivityByChannelId(channelId);
    }

}
