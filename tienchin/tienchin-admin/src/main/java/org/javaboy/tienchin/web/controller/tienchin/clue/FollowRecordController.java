package org.javaboy.tienchin.web.controller.tienchin.clue;

import org.javaboy.tienchin.clue.service.IFollowRecordService;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2022-12-14
 */
@RestController
@RequestMapping("/tienchin/clue/follow/record")
public class FollowRecordController {

    @Autowired
    IFollowRecordService followRecordService;

    @PreAuthorize("hasAnyPermissions('tienchin:clue:follow','tienchin:clue:view')")
    @GetMapping("/{clueId}")
    public AjaxResult getFollowRecordByClueId(@PathVariable Integer clueId) {
        return followRecordService.getFollowRecordByClueId(clueId);
    }
}
