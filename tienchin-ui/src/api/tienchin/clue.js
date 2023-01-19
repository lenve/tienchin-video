import request from '@/utils/request'

// 查询渠道列表
export function listChannels(query) {
    return request({
        url: '/tienchin/clue/channels',
        method: 'get',
        params: query
    })
}
// 查询渠道列表
export function getClueById(clueId) {
    return request({
        url: '/tienchin/clue/'+clueId,
        method: 'get'
    })
}
// 查询活动列表
export function listActivity(channelId) {
    return request({
        url: '/tienchin/clue/activity/'+channelId,
        method: 'get'
    })
}

// 查询线索列表
export function listClue() {
    return request({
        url: '/tienchin/clue/list',
        method: 'get'
    })
}
// 根据部门查询用户列表
export function listUsers(deptId) {
    return request({
        url: '/tienchin/clue/users/'+deptId,
        method: 'get'
    })
}

// 查询角色详细
export function getActivity(activityId) {
    return request({
        url: '/tienchin/activity/' + activityId,
        method: 'get'
    })
}

// 新增线索
export function addClue(data) {
    return request({
        url: '/tienchin/clue',
        method: 'post',
        data: data
    })
}
// 线索分配
export function assignClue(data) {
    return request({
        url: '/tienchin/assignment',
        method: 'post',
        data: data
    })
}

// 修改角色
export function updateActivity(data) {
    return request({
        url: '/tienchin/activity',
        method: 'put',
        data: data
    })
}

// 删除角色
export function delActivity(activityIds) {
    return request({
        url: '/tienchin/activity/' + activityIds,
        method: 'delete'
    })
}
