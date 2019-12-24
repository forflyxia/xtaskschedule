package com.tc.xtaskschedule.repository.mapper;

import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.service.task.dynamicsql.customsql.CustomSqlWrapper;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.TableSqlWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by hbxia on 2017/3/27.
 */
@Repository
public interface DataSyncMapper {

    /**
     * 返回Item Key,Datachange_Lasttime
     */
    List<DataSyncPo> executeCustomSql(CustomSqlWrapper customSqlWrapper);

    /**
     * 返回自定义sql所有字段信息
     */
    List<Map<String, Object>> executeFullCustomSql(CustomSqlWrapper customSqlWrapper);

    List<DataSyncPo> executeTableSqlEquals(TableSqlWrapper tableSqlWrapper);

    List<DataSyncPo> executeTableSqlGreater(TableSqlWrapper tableSqlWrapper);

    List<Map<String, Object>> executeFullTableSqlEquals(TableSqlWrapper tableSqlWrapper);

    List<Map<String, Object>> executeFullTableSqlGreater(TableSqlWrapper tableSqlWrapper);
}
