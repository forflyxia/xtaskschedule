package com.tc.xtaskschedule.repository.po.mysql.taskdb;

import java.util.Date;

/**
 * Created by hbxia on 2017/4/21.
 */
public class TaskTodoItemsPo {

    private int id;
    private String batchNo;
    private int taskId;
    private String itemKey;
    private Date itemKey_DataChange_LastTime;
    private String extendData;
    private int shardingNo;
    private int status;
    private int priority;
    private String remark;
    private Date createTime;

    public TaskTodoItemsPo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public Integer getShardingNo() {
        return shardingNo;
    }

    public void setShardingNo(Integer shardingNo) {
        this.shardingNo = shardingNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getItemKey_DataChange_LastTime() {
        return itemKey_DataChange_LastTime;
    }

    public void setItemKey_DataChange_LastTime(Date itemKey_DataChange_LastTime) {
        this.itemKey_DataChange_LastTime = itemKey_DataChange_LastTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getExtendData() {
        return extendData;
    }

    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }
}
