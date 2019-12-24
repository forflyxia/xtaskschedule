package com.tc.xtaskschedule.repository;

import com.tc.xtaskschedule.repository.mapper.mysql.taskdb.TaskMapper;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskTodoItemsPo;
import com.tc.xtaskschedule.service.task.TaskConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hbxia on 2017/4/18.
 */
@Repository
public class TaskRepository {

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskConfig> getTasks() {
        List<TaskConfig> taskConfigs = new ArrayList<>();
        List<TaskPo> tasks = taskMapper.getTasks();
        for (TaskPo task : tasks) {
            taskConfigs.add(convert(task));
        }
        return taskConfigs;
    }

    public TaskIncrementPo getTaskIncrement(int taskId) {

        return taskMapper.getTaskIncrement(taskId);
    }

    public int getTaskTodoItemsCount(Integer taskId) {
        return taskMapper.getTaskTodoItemsCount(taskId);
    }

    public List<TaskTodoItemsPo> getTaskTodoItems(Integer taskId, Integer shardingNo) {
        return taskMapper.getTaskTodoItems(taskId, shardingNo);
    }

    public TaskIncrementPo getDataForSave(int taskId) {
        TaskIncrementPo taskIncrementPo = null;
        if (taskId > 0) {
            taskIncrementPo = getTaskIncrement(taskId);
            if (taskIncrementPo == null) {
                taskIncrementPo = initTaskIncrementPo(taskId);
                taskMapper.insertTaskIncrement(taskIncrementPo);
            }
        } else {
            taskIncrementPo = initTaskIncrementPo(0);
        }
        return taskIncrementPo;
    }

    public boolean saveTaskIncrement(TaskIncrementPo item) {
        return taskMapper.updateTaskIncrement(item);
    }

    public boolean batchInsertTaskTodoItems(List<TaskTodoItemsPo> items) {
        if (CollectionUtils.isEmpty(items)) {
            return true;
        }
        return taskMapper.batchInsertTaskTodoItems(items);
    }

    public boolean updateTaskTodoItemsStatus(Integer id, Integer status) {
        if (id == null) {
            return true;
        }
        return taskMapper.updateTaskTodoItemsStatus(id, status);
    }

    public boolean updateTaskTodoItemsRemark(Integer id, String remark) {
        if (id == null) {
            return true;
        }
        return taskMapper.updateTaskTodoItemsRemark(id, remark);
    }

    private TaskIncrementPo initTaskIncrementPo(int taskId) {
        TaskIncrementPo item = new TaskIncrementPo();
        item.setTaskId(taskId);
        item.setHasNewData(1);
        item.setMaxKey_LastTime("0");
        item.setMaxTime_LastTime("1900-01-01");
        item.setMinKey_LastTime("0");
        item.setMinTime_LastTime("1900-01-01");
        item.setStatus(1);
        return item;
    }

    /**
     *
     * @param item
     * @return
     */
    private TaskConfig convert(TaskPo item) {
        TaskConfig config = new TaskConfig();
        config.setTaskId(item.getTaskId());
        config.setTaskName(item.getTaskName());
        config.setNThreadsPerNode(item.getNThreadsPerNode());
        config.setTaskArgs(item.getTaskArgs());
        config.setRowsPerTime(item.getRowsPerTime());
        config.setDbType(item.getDbType());
        config.setSqlExecutorType(item.getSqlExecutorType());
        config.setDbName(item.getDbName());
        config.setTableName(item.getTableName());
        config.setKeyColumnName(item.getKeyColumnName());
        config.setKeyColumnType(item.getKeyColumnType());
        config.setCustomSqlEquals(item.getCustomSqlEquals());
        config.setCustomSqlGreater(item.getCustomSqlGreater());
        config.setTaskExecutorType(item.getTaskExecutorType());
        config.setDestinationAddress(item.getDestinationAddress());
        config.setFailedRetryTimes(item.getFailedRetryTimes());
        config.setInterval(item.getInterval());
        config.setHostNodes(item.getHostNodes());
        config.setDownload(item.getIsDownload() == 1);
        config.setLoop(item.getIsLoop() == 1);
        config.setVersion(item.getVersion());
        return config;
    }
}
