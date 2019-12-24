package com.tc.xtaskschedule.repository.po.mysql.taskdb;

/**
 * Created by hbxia on 2017/4/21.
 */
public class TaskIncrementPo {
    private int taskId;
    private String minKey_LastTime;
    private String minTime_LastTime;
    private String maxKey_LastTime;
    private String maxTime_LastTime;
    private int hasNewData;
    private int status;

    public TaskIncrementPo() {

    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getMinKey_LastTime() {
        return minKey_LastTime;
    }

    public void setMinKey_LastTime(String minKey_LastTime) {
        this.minKey_LastTime = minKey_LastTime;
    }

    public String getMinTime_LastTime() {
        return minTime_LastTime;
    }

    public void setMinTime_LastTime(String minTime_LastTime) {
        this.minTime_LastTime = minTime_LastTime;
    }

    public String getMaxKey_LastTime() {
        return maxKey_LastTime;
    }

    public void setMaxKey_LastTime(String maxKey_LastTime) {
        this.maxKey_LastTime = maxKey_LastTime;
    }

    public String getMaxTime_LastTime() {
        return maxTime_LastTime;
    }

    public void setMaxTime_LastTime(String maxTime_LastTime) {
        this.maxTime_LastTime = maxTime_LastTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHasNewData() {
        return hasNewData;
    }

    public void setHasNewData(int hasNewData) {
        this.hasNewData = hasNewData;
    }
}
