package com.tc.xtaskschedule.service.task.dynamicsql.customsql;

import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.service.task.TaskConfig;

import java.util.List;

public interface CustomDynamicSql {

    List<DataSyncPo> getEqualsItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement);

    List<DataSyncPo> getGreaterItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement, int existedCounts);

    List<DataSyncPo> getFullEqualsItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement);

    List<DataSyncPo> getFullGreaterItems(TaskConfig taskConfig, TaskIncrementPo taskIncrement, int existedCounts);

}
