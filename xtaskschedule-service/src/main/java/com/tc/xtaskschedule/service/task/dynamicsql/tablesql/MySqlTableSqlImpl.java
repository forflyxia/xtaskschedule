package com.tc.xtaskschedule.service.task.dynamicsql.tablesql;

import com.tc.xtaskschedule.repository.DataSyncRepository;
import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.service.task.TaskConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbxia on 2017/4/11.
 */
public class MySqlTableSqlImpl implements TableDynamicSql {

    @Override
    public List<DataSyncPo> getEqualsItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement) {
        TableSqlWrapper wrapper = new TableSqlWrapper(taskConfig, taskIncrement, taskConfig.getRowsPerTime());
        return DataSyncRepository.executeTableSqlEquals(wrapper);
    }

    @Override
    public List<DataSyncPo> getGreaterItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement, int existedCounts) {
        int topN = taskConfig.getRowsPerTime() - existedCounts;
        if (topN > 0) {
            TableSqlWrapper wrapper = new TableSqlWrapper(taskConfig, taskIncrement, topN);
            return DataSyncRepository.executeTableSqlGreater(wrapper);
        }
        return new ArrayList<>();
    }

    @Override
    public List<DataSyncPo> getFullEqualsItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement) {
        TableSqlWrapper wrapper = new TableSqlWrapper(taskConfig, taskIncrement, taskConfig.getRowsPerTime());
        return DataSyncRepository.executeFullTableSqlEquals(wrapper);
    }

    @Override
    public List<DataSyncPo> getFullGreaterItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement, int existedCounts) {
        int topN = taskConfig.getRowsPerTime() - existedCounts;
        if (topN > 0) {
            TableSqlWrapper wrapper = new TableSqlWrapper(taskConfig, taskIncrement, topN);
            return DataSyncRepository.executeFullTableSqlGreater(wrapper);
        }
        return new ArrayList<>();
    }
}
