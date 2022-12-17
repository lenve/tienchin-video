package org.javaboy.ask_for_leave_demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 *
 * 接收前端传来的请假参数
 */
public class AskForLeaveVO {
    private Integer days;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date endTime;
    //请假的审批人
    private String approveUser;
    private String processId;
    private Boolean approval;

    public AskForLeaveVO(Integer days, String reason, Date startTime, Date endTime, String approveUser, String processId, Boolean approval) {
        this.days = days;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.approveUser = approveUser;
        this.processId = processId;
        this.approval = approval;
    }

    public Boolean getApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public AskForLeaveVO() {
    }

    public AskForLeaveVO(Integer days, String reason, Date startTime, Date endTime, String approveUser,String processId) {
        this.days = days;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.approveUser = approveUser;
        this.processId = processId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }
}
