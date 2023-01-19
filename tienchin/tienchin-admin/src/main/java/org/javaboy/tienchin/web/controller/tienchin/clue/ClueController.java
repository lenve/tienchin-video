package org.javaboy.tienchin.web.controller.tienchin.clue;

import org.javaboy.tienchin.activity.domain.vo.ActivityVO;
import org.javaboy.tienchin.activity.service.IActivityService;
import org.javaboy.tienchin.channel.domain.vo.ChannelVO;
import org.javaboy.tienchin.channel.service.IChannelService;
import org.javaboy.tienchin.clue.domain.Clue;
import org.javaboy.tienchin.clue.domain.vo.ClueSummary;
import org.javaboy.tienchin.clue.service.IClueService;
import org.javaboy.tienchin.common.annotation.Log;
import org.javaboy.tienchin.common.core.controller.BaseController;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.core.page.TableDataInfo;
import org.javaboy.tienchin.common.enums.BusinessType;
import org.javaboy.tienchin.course.domain.Course;
import org.javaboy.tienchin.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2022-12-14
 */
@RestController
@RequestMapping("/tienchin/clue")
public class ClueController extends BaseController {

    @Autowired
    IClueService clueService;
    @Autowired
    IChannelService channelService;

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    IActivityService activityService;

    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @Log(title = "线索管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Clue clue) {
        return clueService.addClue(clue);
    }

    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @GetMapping("/channels")
    public AjaxResult getAllChannels() {
        return AjaxResult.success(channelService.list());
    }

    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @GetMapping("/activity/{channelId}")
    public AjaxResult getActivityByChannelId(@PathVariable Integer channelId) {
        return activityService.selectActivityByChannelId(channelId);
    }

    @PreAuthorize("hasPermission('tienchin:clue:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<ClueSummary> list = clueService.selectClueList();
        return getDataTable(list);
    }


    @GetMapping("/users/{deptId}")
    @PreAuthorize("hasPermission('tienchin:clue:assignment')")
    public AjaxResult getUsersByDeptId(@PathVariable Long deptId) {
        return sysUserService.getUsersByDeptId(deptId);
    }


    @GetMapping("/{clueId}")
    @PreAuthorize("hasAnyPermissions('tienchin:clue:view','tienchin:clue:follow')")
    public AjaxResult getClueDetailsByClueId(@PathVariable Integer clueId) {
        return clueService.getClueDetailsByClueId(clueId);
    }
}
