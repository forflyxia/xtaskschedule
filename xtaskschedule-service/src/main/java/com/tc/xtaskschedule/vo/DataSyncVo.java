package com.tc.xtaskschedule.vo;

import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;

import java.util.List;

public class DataSyncVo {

    private List<DataSyncPo> syncItems;

    public DataSyncVo(List<DataSyncPo> items) {
        this.syncItems = items;
    }

    public List<DataSyncPo> getSyncItems() {
        return syncItems;
    }

    public void setSyncItems(List<DataSyncPo> syncItems) {
        this.syncItems = syncItems;
    }

    public DataSyncPo getFirstItem() {
        if (!this.syncItems.isEmpty()) {
            return syncItems.get(0);
        }
        return null;
    }

    public DataSyncPo getLastItem() {
        if (!this.syncItems.isEmpty()) {
            return syncItems.get(syncItems.size() - 1);
        }
        return null;
    }
}
