package com.tc.xtaskschedule.service.task;

import com.tc.xtaskschedule.selfschedule.HeartBeatContext;

/**
 * Created by hbxia on 2017/3/23.
 */
public class TaskContext {

    private TaskConfig taskConfig;
    private volatile HeartBeatContext heartBeatContext;
    private String selfScheduleBatchNo;

    /**
     *  该任务是在哪次调度批次下执行的
     */
    private String executeBatchNo;

    public TaskConfig getTaskConfig() {
        return taskConfig;
    }

    public void setTaskConfig(TaskConfig taskConfig) {
        this.taskConfig = taskConfig;
    }

    public HeartBeatContext getHeartBeatContext() {
        return heartBeatContext;
    }

    public void setHeartBeatContext(HeartBeatContext heartBeatContext) {
        this.heartBeatContext = heartBeatContext;
    }

    public String getSelfScheduleBatchNo() {
        return selfScheduleBatchNo;
    }

    public void setSelfScheduleBatchNo(String batchNo) {
        this.selfScheduleBatchNo = batchNo;
    }

    public String getExecuteBatchNo() {
        return executeBatchNo;
    }

    public void setExecuteBatchNo(String executeBatchNo) {
        this.executeBatchNo = executeBatchNo;
    }
}
