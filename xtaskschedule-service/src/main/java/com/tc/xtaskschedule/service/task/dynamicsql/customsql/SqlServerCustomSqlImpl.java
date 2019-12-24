package com.tc.xtaskschedule.service.task.dynamicsql.customsql;

import com.tc.xtaskschedule.repository.DataSyncRepository;
import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.service.task.TaskConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbxia on 2017/4/11.
 */
public class SqlServerCustomSqlImpl implements CustomDynamicSql {

    @Override
    public List<DataSyncPo> getEqualsItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement) {
        String sql = taskConfig.getCustomSqlEquals();
        sql = sql.replace("@count", String.valueOf(taskConfig.getRowsPerTime()));
        sql = sql.replace("@datachange_lasttime", String.format("'%s'", taskIncrement.getMaxTime_LastTime()));
        sql = sql.replace("@itemkey", String.format("'%s'", taskIncrement.getMaxKey_LastTime()));
        CustomSqlWrapper wrapper = new CustomSqlWrapper(sql);
        return DataSyncRepository.executeCustomSql(wrapper);
    }

    @Override
    public List<DataSyncPo> getGreaterItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement, int existedCounts) {
        int topN = taskConfig.getRowsPerTime() - existedCounts;
        if (topN > 0) {
            String sql = taskConfig.getCustomSqlGreater();
            sql = sql.replace("@count", String.valueOf(taskConfig.getRowsPerTime()));
            sql = sql.replace("@datachange_lasttime", String.format("'%s'", taskIncrement.getMaxTime_LastTime()));
            CustomSqlWrapper wrapper = new CustomSqlWrapper(sql);
            return DataSyncRepository.executeCustomSql(wrapper);
        }
        return new ArrayList<>();
    }

    @Override
    public List<DataSyncPo> getFullEqualsItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement) {
        String sql = taskConfig.getCustomSqlEquals();
        sql = sql.replace("@count", String.valueOf(taskConfig.getRowsPerTime()));
        sql = sql.replace("@datachange_lasttime", String.format("'%s'", taskIncrement.getMaxTime_LastTime()));
        sql = sql.replace("@itemkey", String.format("'%s'", taskIncrement.getMaxKey_LastTime()));
        CustomSqlWrapper wrapper = new CustomSqlWrapper(sql);
        return DataSyncRepository.executeFullCustomSql(wrapper);
    }

    @Override
    public List<DataSyncPo> getFullGreaterItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement, int existedCounts) {
        int topN = taskConfig.getRowsPerTime() - existedCounts;
        if (topN > 0) {
            String sql = taskConfig.getCustomSqlGreater();
            sql = sql.replace("@count", String.valueOf(taskConfig.getRowsPerTime()));
            sql = sql.replace("@datachange_lasttime", String.format("'%s'", taskIncrement.getMaxTime_LastTime()));
            CustomSqlWrapper wrapper = new CustomSqlWrapper(sql);
            return DataSyncRepository.executeFullCustomSql(wrapper);
        }
        return new ArrayList<>();
    }
}
