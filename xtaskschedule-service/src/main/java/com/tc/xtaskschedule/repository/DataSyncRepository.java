package com.tc.xtaskschedule.repository;

import cn.tcc.foundation.core.convert.StringTo;
import cn.tcc.foundation.core.serialization.GsonSerializer;
import com.tc.xtaskschedule.repository.mapper.DataSyncMapper;
import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.TableSqlWrapper;
import com.tc.xtaskschedule.service.task.dynamicsql.customsql.CustomSqlWrapper;
import com.tc.xtaskschedule.SpringContext;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hbxia on 2017/3/30.
 */
public class DataSyncRepository {

    private static final DataSyncMapper dataSyncMapper = (DataSyncMapper) SpringContext.getBean("dataSyncMapper");

    public static List<DataSyncPo> executeCustomSql(CustomSqlWrapper customSqlWrapper) {
        if (customSqlWrapper == null || StringUtils.isEmpty(customSqlWrapper.getSql())) {
            return new ArrayList<>();
        }
        return dataSyncMapper.executeCustomSql(customSqlWrapper);
    }

    public static List<DataSyncPo> executeFullCustomSql(CustomSqlWrapper customSqlWrapper) {
        if (customSqlWrapper == null || StringUtils.isEmpty(customSqlWrapper.getSql())) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> mapItems = dataSyncMapper.executeFullCustomSql(customSqlWrapper);
        return getDataSyncs(mapItems);
    }

    public static List<DataSyncPo> executeTableSqlEquals(TableSqlWrapper tableSqlWrapper) {
        return dataSyncMapper.executeTableSqlEquals(tableSqlWrapper);
    }

    public static List<DataSyncPo> executeTableSqlGreater(TableSqlWrapper tableSqlWrapper) {
        return dataSyncMapper.executeTableSqlGreater(tableSqlWrapper);
    }

    public static List<DataSyncPo>  executeFullTableSqlEquals(TableSqlWrapper tableSqlWrapper) {
        List<Map<String, Object>> mapItems = dataSyncMapper.executeFullTableSqlEquals(tableSqlWrapper);
        return getDataSyncs(mapItems);
    }

    public static List<DataSyncPo> executeFullTableSqlGreater(TableSqlWrapper tableSqlWrapper) {
        List<Map<String, Object>> mapItems = dataSyncMapper.executeFullTableSqlGreater(tableSqlWrapper);
        return getDataSyncs(mapItems);
    }

    private static List<DataSyncPo> getDataSyncs(List<Map<String, Object>> mapItems) {
        if (mapItems == null) {
            return new ArrayList<>();
        }
        List<DataSyncPo> items = new ArrayList<>();
        for (Map<String, Object> map : mapItems) {
            DataSyncPo item = new DataSyncPo();
            item.setItemKey(map.get("itemKey").toString());
            item.setDataChange_LastTime(StringTo.toyMdHmsSDate(map.get("dataChange_LastTime").toString()));

            map.remove("itemKey");
            map.remove("dataChange_LastTime");
            String json = GsonSerializer.toJson(map);
            item.setExtendData(json);
            items.add(item);
        }
        return items;
    }
}
