package com.tc.xtaskschedule.service.task;

import cn.tcc.foundation.core.convert.DateTimeTo;
import cn.tcc.foundation.core.convert.ListTo;
import cn.tcc.foundation.core.log.TraceLogBuilder;
import cn.tcc.foundation.core.serialization.FastJsonSerializer;
import com.tc.xtaskschedule.datasource.DynamicDataSource;
import com.tc.xtaskschedule.repository.TaskRepository;
import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskTodoItemsPo;
import com.tc.xtaskschedule.service.Task;
import com.tc.xtaskschedule.SpringContext;
import com.tc.xtaskschedule.vo.DataSyncVo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by hbxia on 2017/3/23.
 */
public abstract class BaseTask implements Task {

    private ThreadPoolExecutor poolExecutor;
    private TaskContext taskContext;
    private volatile LocalDateTime latestExecuteTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    private volatile TaskEnum.TaskStatus status = TaskEnum.TaskStatus.STOPPED;
    protected static final TaskRepository taskRepository = (TaskRepository)SpringContext.getBean("taskRepository");

    public BaseTask(TaskContext context) {
        this.taskContext = context;
        this.poolExecutor = new ThreadPoolExecutor(0, this.taskContext.getTaskConfig().getNThreadsPerNode(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public TaskEnum.TaskStatus getStatus() {
        return status;
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public TaskConfig getTaskConfig() {
        return this.taskContext.getTaskConfig();
    }

    public String getTaskName() { return this.getTaskConfig().getTaskName(); }

    @Override
    public void start() {
        try {
            if (startTask()) {
                if (validTask()) {
                    // 下次调度才生效
                    int myShardingIndex = this.getTaskContext().getHeartBeatContext().myShardingIndex;
                    boolean isMaster = myShardingIndex == 1;
                    TaskIncrementPo taskIncrementPo = taskRepository.getDataForSave(this.getTaskConfig().getTaskId());

                    DataSyncVo syncVo = null;
                    List<DataSyncPo> syncItems;
                    if (this.getTaskContext().getTaskConfig().isDownload()) {
                        if (isMaster) {
                            switchTaskDataSource(this.getTaskConfig().getDbName());
                            syncVo = loadData(taskIncrementPo);
                            assignDataSyncs(this.getTaskConfig().getTaskId(), syncVo.getSyncItems());
                        }
                        syncItems = getTaskTodoItems(this.getTaskConfig().getTaskId(), myShardingIndex);
                    } else {
                        switchTaskDataSource(this.getTaskConfig().getDbName());
                        syncVo = loadData(taskIncrementPo);
                        syncItems = syncVo.getSyncItems();
                    }
                    TraceLogBuilder.appendLine("[%s]Sharding-%s加载数据总量[%s].", this.getTaskName(), myShardingIndex, syncItems.size());

                    List<DataSyncPo> failedItems = batchRunSyncItems(syncItems);
                    if (!failedItems.isEmpty()) {
                        int retryTimes = this.taskContext.getTaskConfig().getFailedRetryTimes();
                        while (retryTimes > 0) {
                            TraceLogBuilder.appendLine("[%s]重试次数[%s].", this.getTaskName(), retryTimes);
                            failedItems = batchRunSyncItems(failedItems);
                            retryTimes--;
                        }
                    }

                    boolean hasFailedItems = failedItems.size() > 0;
                    if (syncVo != null) {
                        if (this.getTaskConfig().isDownload()) {
                            if (isMaster) {
                                logTaskIncrement(taskIncrementPo, syncVo.getFirstItem(), syncVo.getLastItem());
                            }
                        } else {
                            logTaskIncrement(taskIncrementPo, syncVo.getFirstItem(), syncVo.getLastItem());
                        }
                    }
                }
                stopTask();
            }
        } catch (Exception ex) {
            stopTask();
            //TODO:可以考虑删除该批次数据
            TraceLogBuilder.appendLine("[%s]本次运行数据处理异常[%s].", this.getTaskName(), ex.getMessage());
        } finally {

        }
    }

    @Override
    public void stop() {
        if (this.status != TaskEnum.TaskStatus.STOPPED) {
            stopTask();
        }
    }

    public abstract DataSyncVo loadData(TaskIncrementPo taskIncrementPo);

    public abstract Callable<BatchTaskContext> execute(BatchTaskContext batchTaskContext);

    private boolean validTask() {
        TaskConfig taskConfig = this.getTaskConfig();
        int interval = taskConfig.getInterval();
        LocalDateTime nextTime = this.latestExecuteTime.plusSeconds(interval);
        if (nextTime.compareTo(LocalDateTime.now()) > 0) {
            TraceLogBuilder.appendLine("[%s]调度周期未到(下次执行时间：%s).", taskConfig.getTaskName(), nextTime);
            return false;
        }
        return true;
    }

    private boolean assignDataSyncs(Integer taskId, List<DataSyncPo> syncItems) {
        String batchNo = this.taskContext.getSelfScheduleBatchNo();
        int executorNodes = this.taskContext.getHeartBeatContext().shardings;
        int myShardingIndex = this.taskContext.getHeartBeatContext().myShardingIndex;
        TraceLogBuilder.appendLine("[%s]被设置为落地数据,开始分配数据,执行节点数为%s,当前节点索引为%s.", this.getTaskName(), executorNodes, myShardingIndex);
        if (myShardingIndex == 1) {
            //TODO:分析数据分布，决定是否需要继续落地数据或异步重新分配数据

            Map<Integer, List<DataSyncPo>> groupMap = ListTo.toGroup(syncItems, executorNodes);
            for (Map.Entry<Integer, List<DataSyncPo>> entry : groupMap.entrySet()) {
                List<TaskTodoItemsPo> items = new ArrayList<>();
                for (DataSyncPo dataSyncPo : entry.getValue()) {
                    TaskTodoItemsPo todoItemsPo = new TaskTodoItemsPo();
                    todoItemsPo.setBatchNo(batchNo);
                    todoItemsPo.setItemKey(dataSyncPo.getItemKey());
                    todoItemsPo.setItemKey_DataChange_LastTime(dataSyncPo.getDataChange_LastTime());
                    if (dataSyncPo.getExtendData() == null) {
                        todoItemsPo.setExtendData("");
                    } else {
                        todoItemsPo.setExtendData(dataSyncPo.getExtendData());
                    }
                    todoItemsPo.setTaskId(taskId);
                    todoItemsPo.setShardingNo(entry.getKey());
                    todoItemsPo.setStatus(1);
                    items.add(todoItemsPo);
                }
                TraceLogBuilder.appendLine("[%s]落地批次%s,数据条数%s.", this.getTaskName(), entry.getKey(), items.size());
                taskRepository.batchInsertTaskTodoItems(items);
            }
        }
        return true;
    }

    private List<DataSyncPo> getTaskTodoItems(Integer taskId, Integer shardingNo) {
        List<TaskTodoItemsPo> todoItemsPos = taskRepository.getTaskTodoItems(taskId, shardingNo);
        TraceLogBuilder.appendLine("[%s]Sharding-%s从落地数据中获取数据,数据条数%s.", this.getTaskName(), shardingNo, todoItemsPos.size());
        List<DataSyncPo> items = new ArrayList<>();
        for (TaskTodoItemsPo todoItemsPo : todoItemsPos) {
            DataSyncPo item = new DataSyncPo();
            item.setId(todoItemsPo.getId());
            item.setItemKey(todoItemsPo.getItemKey());
            item.setDataChange_LastTime(todoItemsPo.getItemKey_DataChange_LastTime());
            item.setExtendData(todoItemsPo.getExtendData());
            items.add(item);
        }
        return items;
    }

    private void switchTaskDataSource(String dbKey) {
        DynamicDataSource.setDataSourceKey(dbKey);
    }

    private List<DataSyncPo> batchRunSyncItems(List<DataSyncPo> syncItems) {
        Map<Integer, List<DataSyncPo>> batchMap = ListTo.toGroup(syncItems, this.getTaskConfig().getNThreadsPerNode());
        TraceLogBuilder.appendLine("[%s]开始分批[总批次数%s]处理数据.", this.getTaskName(), batchMap.size());

        List<Future<?>> futures = new ArrayList<>();
        for (Map.Entry<Integer, List<DataSyncPo>> entry : batchMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                BatchTaskContext batchTaskContext = new BatchTaskContext(this, entry.getKey(), entry.getValue());
                Callable<?> callable = execute(batchTaskContext);
                Future<?> future = poolExecutor.submit(callable);
                futures.add(future);
            }
        }

        List<DataSyncPo> failedItems = new ArrayList<>();
        for (Future<?> future : futures) {
            try {
                BatchTaskContext batchTaskContext = (BatchTaskContext) future.get();
                if (!batchTaskContext.getFailedBatchItems().isEmpty()) {
                    TraceLogBuilder.appendLine("[%s]批次[%s]未正确处理数据量[%s].", this.getTaskName(), batchTaskContext.getBatchNumber(), batchTaskContext.getFailedBatchItems().size());
                    failedItems.addAll(batchTaskContext.getFailedBatchItems());
                }
            } catch (InterruptedException e) {
                TraceLogBuilder.appendLine("[%s]执行异常:%s.", this.getTaskName(), e.getMessage());
            } catch (ExecutionException e) {
                TraceLogBuilder.appendLine("[%s]执行异常:%s.", this.getTaskName(), e.getMessage());
            }
        }
        return failedItems;
    }

    private boolean startTask() {
        if (this.status == TaskEnum.TaskStatus.RUNNING) {
            TraceLogBuilder.appendLine("[%s]正在运行中,本次执行已退出...", this.getTaskName());
            return false;
        }

        this.status = TaskEnum.TaskStatus.RUNNING;
        this.getTaskContext().setExecuteBatchNo(this.getTaskContext().getSelfScheduleBatchNo());
        TraceLogBuilder.appendLine("[%s]开始执行...", this.getTaskName());
        return true;
    }

    private void stopTask() {
        this.status = TaskEnum.TaskStatus.STOPPED;
        this.getTaskContext().setExecuteBatchNo("");
        TraceLogBuilder.appendLine("[%s]结束执行.", this.getTaskName());
    }

    private boolean logTaskIncrement(TaskIncrementPo item, DataSyncPo firstItem, DataSyncPo lastItem) {
        int hasNewData = (firstItem == null || lastItem == null) ? 0 : 1;
        if (firstItem != null) {
            item.setMinKey_LastTime(firstItem.getItemKey());
            item.setMinTime_LastTime(DateTimeTo.toyMdHsSString(firstItem.getDataChange_LastTime()));
        }
        if (lastItem != null) {
            item.setMaxKey_LastTime(lastItem.getItemKey());
            item.setMaxTime_LastTime(DateTimeTo.toyMdHsSString(lastItem.getDataChange_LastTime()));
        }
        if (this.getTaskConfig().isLoop() && hasNewData == 0) {
            item.setMinKey_LastTime("0");
            item.setMaxKey_LastTime("0");
            item.setMinTime_LastTime("1900-01-01");
            item.setMaxTime_LastTime("1900-01-01");
            TraceLogBuilder.appendLine("[%s]重置循环执行", this.getTaskName());
        }
        item.setHasNewData(hasNewData);
        taskRepository.saveTaskIncrement(item);
        TraceLogBuilder.appendLine("[%s]更新的增量信息[%s]", this.getTaskName(), FastJsonSerializer.toString(item));
        this.latestExecuteTime = LocalDateTime.now();
        TraceLogBuilder.appendLine("[%s]更新上下文上次执行时间[%s]", this.getTaskName(), LocalDateTime.now());
        return true;
    }
}


