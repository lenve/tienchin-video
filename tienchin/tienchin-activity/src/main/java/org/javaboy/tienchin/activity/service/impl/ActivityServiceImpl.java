package org.javaboy.tienchin.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.javaboy.tienchin.activity.domain.Activity;
import org.javaboy.tienchin.activity.domain.vo.ActivityVO;
import org.javaboy.tienchin.activity.mapper.ActivityMapper;
import org.javaboy.tienchin.activity.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2022-12-11
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Override
    public List<ActivityVO> selectActivityList(ActivityVO activityVO) {
        expireActivity();
        return activityMapper.selectActivityList(activityVO);
    }

    @Override
    public AjaxResult addActivity(ActivityVO activityVO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityVO, activity);
        activity.setCreateTime(LocalDateTime.now());
        activity.setCreateBy(SecurityUtils.getUsername());
        activity.setDelFlag(0);
        //设置活动未过期，相当于新增的活动，默认都是未过期的
        activity.setStatus(1);
        return save(activity) ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }

    @Override
    public AjaxResult updateActivity(ActivityVO activityVO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityVO, activity);
        activity.setUpdateBy(SecurityUtils.getUsername());
        activity.setUpdateTime(LocalDateTime.now());
        //将来更新的时候，不修改为 null 的字段
        activity.setCreateBy(null);
        activity.setCreateTime(null);
        activity.setDelFlag(null);
        return updateById(activity) ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }

    @Override
    public ActivityVO getActivityById(Long activityId) {
        Activity activity = getById(activityId);
        ActivityVO activityVO = new ActivityVO();
        BeanUtils.copyProperties(activity, activityVO);
        return activityVO;
    }

    @Override
    public boolean deleteActivityByIds(Long[] activityIds) {
        UpdateWrapper<Activity> uw = new UpdateWrapper<>();
        uw.lambda().set(Activity::getDelFlag, 1).in(Activity::getActivityId, activityIds);
        return update(uw);
    }

    /**
     * 将超过当前时间的活动设置为过期
     *
     * @return
     */
    public boolean expireActivity() {
        UpdateWrapper<Activity> uw = new UpdateWrapper<>();
        //将原本状态为 1 并且 endTime 小于当前时间的活动，设置为过期
        uw.lambda().set(Activity::getStatus, 0).eq(Activity::getStatus, 1).lt(Activity::getEndTime, LocalDateTime.now());
        return update(uw);
    }
}
