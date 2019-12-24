package com.tc.xtaskschedule.repository.po.mysql;

import java.util.Date;

/**
 * Created by hbxia on 2017/3/29.
 */
public class DataSyncPo {

    private Integer Id;
    private String itemKey;
    private Date dataChange_LastTime;
    private String extendData;

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public Date getDataChange_LastTime() {
        return dataChange_LastTime;
    }

    public void setDataChange_LastTime(Date dataChange_LastTime) {
        this.dataChange_LastTime = dataChange_LastTime;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getExtendData() {
        return extendData;
    }

    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }
}
