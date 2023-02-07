package org.javaboy.tienchin.clue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.javaboy.tienchin.clue.domain.FollowRecord;
import org.javaboy.tienchin.clue.mapper.FollowRecordMapper;
import org.javaboy.tienchin.clue.service.IFollowRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.common.constant.TienChinConstants;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2022-12-14
 */
@Service
public class FollowRecordServiceImpl extends ServiceImpl<FollowRecordMapper, FollowRecord> implements IFollowRecordService {

    @Override
    public AjaxResult getFollowRecordByClueId(Integer clueId) {
        QueryWrapper<FollowRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(FollowRecord::getType, TienChinConstants.CLUE_TYPE).eq(FollowRecord::getAssignId, clueId).orderByDesc(FollowRecord::getCreateTime);
        return AjaxResult.success(list(qw));
    }
}
