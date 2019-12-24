package com.tc.xtaskschedule.service;

import cn.tcc.foundation.core.log.TraceLogBuilder;
import com.tc.xtaskschedule.service.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hbxia on 2017/3/23.
 */
@Service
public class TaskProvider {

    @Autowired
    private RunningLogPool runningLogPool;

    private static final Map<String, BaseTask> All_TASKS = new HashMap<String, BaseTask>();
    private static final Object LOCK_OBJ = new Object();

    public BaseTask getTask(TaskContext latestContext) {
        TaskConfig latestTaskConfig = latestContext.getTaskConfig();
        if (!All_TASKS.containsKey(latestTaskConfig.getTaskName())) {
            synchronized (LOCK_OBJ) {
                if (!All_TASKS.containsKey(latestTaskConfig.getTaskName())) {
                    TraceLogBuilder.appendLine("[%s]任务池中不存在,创建中...", latestTaskConfig.getTaskName());
                    BaseTask task = TaskFactory.create(latestContext);
                    All_TASKS.put(latestTaskConfig.getTaskName(), task);
                }
            }
        } else {
            TraceLogBuilder.appendLine("[%s]缓存任务池中存在.", latestTaskConfig.getTaskName());
            BaseTask existedTask = All_TASKS.get(latestTaskConfig.getTaskName());
            TaskContext oldTaskContext = existedTask.getTaskContext();
            TaskConfig oldTaskConfig = oldTaskContext.getTaskConfig();
            if (latestTaskConfig.getVersion() > oldTaskConfig.getVersion()) {
                synchronized (LOCK_OBJ) {
                    if (latestTaskConfig.getVersion() > oldTaskConfig.getVersion()){
                        TraceLogBuilder.appendLine("[%s]配置变化，版本号由%s(原任务,SelfScheduleBatchNo:%s)->%s(新任务)，原任务准备结束执行...", latestTaskConfig.getTaskName(), oldTaskConfig.getVersion(), oldTaskContext.getExecuteBatchNo(), latestTaskConfig.getVersion());
                        existedTask.stop();
                        BaseTask newTask = TaskFactory.create(latestContext);
                        All_TASKS.replace(latestTaskConfig.getTaskName(), newTask);
                    }
                }
            }
        }
        BaseTask cachedTask = All_TASKS.get(latestTaskConfig.getTaskName());
        int shardingsBefore = cachedTask.getTaskContext().getHeartBeatContext().shardings;
        int shardingsNow = latestContext.getHeartBeatContext().shardings;
        if (shardingsBefore != shardingsNow) {
            TraceLogBuilder.appendLine("[%s]总分片发生变化,之前%s,现在%s.",latestTaskConfig.getTaskName(), shardingsBefore, shardingsNow);
        }
        int myShardingIndexBefore = cachedTask.getTaskContext().getHeartBeatContext().myShardingIndex;
        int myShardingIndexNow = latestContext.getHeartBeatContext().myShardingIndex;
        if (myShardingIndexBefore != myShardingIndexNow) {
            TraceLogBuilder.appendLine("[%s]本机分片发生变化,之前%s,现在%s.",latestTaskConfig.getTaskName(), myShardingIndexBefore, myShardingIndexNow);
        }
        cachedTask.getTaskContext().setSelfScheduleBatchNo(latestContext.getSelfScheduleBatchNo());
        cachedTask.getTaskContext().setHeartBeatContext(latestContext.getHeartBeatContext());
        return cachedTask;
    }

    public boolean runTask(BaseTask task) {

        String batchNo = task.getTaskContext().getSelfScheduleBatchNo();
        TraceLogBuilder.start();
        TraceLogBuilder.appendLine("[%s][%s]自调度开始", batchNo, task.getTaskName());
        if (task != null) {
            task.start();
        }
        TraceLogBuilder.appendLine("[%s][%s]自调度结束",  batchNo, task.getTaskName());
        String message = TraceLogBuilder.end();
        runningLogPool.add(task.getTaskConfig().getTaskName(), message);
        return true;
    }

    public boolean stopTask(BaseTask task) {
        TraceLogBuilder.start();
        if (task != null) {
            task.stop();
        }
        String message = TraceLogBuilder.end();
        return true;
    }


}
