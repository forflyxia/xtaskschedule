package com.tc.xtaskschedule.service.task;


import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;

import java.util.List;

/**
 * Created by hbxia on 2017/4/13.
 */
public class BatchTaskContext {

    private BaseTask task;
    private int batchNumber;
    private List<DataSyncPo> batchItems;
    private List<DataSyncPo> failedBatchItems;

    public BatchTaskContext(BaseTask task, int batchNumber, List<DataSyncPo> batchItems) {
        this.task = task;
        this.batchNumber = batchNumber;
        this.batchItems = batchItems;
    }

    public BaseTask getTask() {
        return task;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public List<DataSyncPo> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(List<DataSyncPo> batchItems) {
        this.batchItems = batchItems;
    }

    public List<DataSyncPo> getFailedBatchItems() {
        return failedBatchItems;
    }

    public void setFailedBatchItems(List<DataSyncPo> failedBatchItems) {
            this.failedBatchItems = failedBatchItems;
        }
}
