package com.tc.xtaskschedule.selfschedule;

import cn.tcc.foundation.core.GuidGenerator;
import cn.tcc.foundation.core.log.TraceLogBuilder;
import com.tc.xtaskschedule.repository.TaskRepository;
import com.tc.xtaskschedule.service.TaskProvider;
import com.tc.xtaskschedule.service.task.BaseTask;
import com.tc.xtaskschedule.service.task.TaskConfig;
import com.tc.xtaskschedule.service.task.TaskContext;
import com.tc.xtaskschedule.service.task.TaskEnum;
import com.tc.xtaskschedule.service.RunningLogPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hbxia on 2017/3/23.
 * 支持自调度Pool
 */
@Service
public class TaskSelfSchedule {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskProvider taskProvider;

    @Autowired
    private RunningLogPool runningLogPool;

    private static ScheduledFuture<?> scheduledFuture;
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);
    private static volatile Boolean initialized = false;
    private static volatile HeartBeatContext heartBeatContext;
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5, new SelfScheduleThreadFactory("SelfScheduleThread", true));

    public TaskSelfSchedule() {

    }

    public void start(HeartBeatContext context) {

        TaskSelfSchedule.heartBeatContext = context;
        if (initialized == false) {
            this.scheduledFuture = this.scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {

                public void run() {
                    String batchNo = GuidGenerator.newGuid();
                    TraceLogBuilder.start();
                    try {
                        TraceLogBuilder.appendLine("批次[%s]自调度子任务开始", batchNo);
                        List<TaskConfig> taskConfigs = taskRepository.getTasks();
                        TraceLogBuilder.appendLine("子任务数量%s", taskConfigs.size());
                        for (TaskConfig taskConfig : taskConfigs) {
                            if (validateTaskConfig(taskConfig)) {
                                TaskContext latestTaskContext = new TaskContext();
                                latestTaskContext.setTaskConfig(taskConfig);
                                latestTaskContext.setHeartBeatContext(heartBeatContext);
                                latestTaskContext.setSelfScheduleBatchNo(batchNo);

                                BaseTask task = taskProvider.getTask(latestTaskContext);
                                if (task.getStatus() == TaskEnum.TaskStatus.RUNNING) {
                                    TraceLogBuilder.appendLine("[%s]正在运行中,本次自调度跳过", taskConfig.getTaskName());
                                    continue;
                                }
                                Future<Boolean> future = scheduledExecutorService.submit(() -> { return taskProvider.runTask(task); });
                            }
                        }
                        TraceLogBuilder.appendLine("批次[%s]自调度子任务结束", batchNo);
                    } catch (Throwable var2) {
                        TraceLogBuilder.appendLine("批次[%s]自调度子任务异常%s", batchNo, var2.getMessage());
                    } finally {
                        String message = TraceLogBuilder.end();
                        runningLogPool.add("selfschedule", message);
                    }
                }

            }, 30* 1000, 10*1000, TimeUnit.MILLISECONDS);
            initialized = true;
        }
    }

    public boolean cancel() {
        Boolean canceled = this.scheduledFuture.cancel(true);
        if (canceled) {
            initialized = false;
        }
        return canceled;
    }

    /**
     *
     * @param latestTaskConfig
     * @return
     */
    private boolean validateTaskConfig(TaskConfig latestTaskConfig) {
        if (latestTaskConfig.getNThreadsPerNode() <= 0) {
            return false;
        }
        if (latestTaskConfig.isDownload() == false) {
            if (StringUtils.isEmpty(latestTaskConfig.getHostNodes())) {
                TraceLogBuilder.appendLine("[%s]被配置为非落地数据,但没有指定hostIp.", latestTaskConfig.getTaskName());
                return false;
            }
            String[] ips = latestTaskConfig.getHostNodes().split(",");
            if (ips.length > 1) {
                TraceLogBuilder.appendLine("[%s]被配置为非落地数据,但指定了多个hostIp.", latestTaskConfig.getTaskName());
                return false;
            }
            String firstIp = ips[0];
            if (firstIp.contains(".")) {

            } else {
                int shardingIndexSetting = Integer.parseInt(ips[0]);
                int myShardingIndex = heartBeatContext.myShardingIndex;
                if (shardingIndexSetting != myShardingIndex) {
                    TraceLogBuilder.appendLine("[%s]设置Sharding%s,当前Sharding%s.",latestTaskConfig.getTaskName(), shardingIndexSetting , myShardingIndex);
                    return false;
                }
            }
        }
        return true;
    }

    static class SelfScheduleThreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum;
        private final String mPrefix;
        private final boolean mDaemo;
        private final ThreadGroup mGroup;

        public SelfScheduleThreadFactory() {
            this("SelfSchedulePool-" + POOL_SEQ.getAndIncrement(), true);
        }

        public SelfScheduleThreadFactory(String prefix) {
            this(prefix, false);
        }

        public SelfScheduleThreadFactory(String prefix, boolean daemo) {
            this.mThreadNum = new AtomicInteger(1);
            this.mPrefix = prefix;
            this.mDaemo = daemo;
            SecurityManager s = System.getSecurityManager();
            this.mGroup = s == null ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }

        public Thread newThread(Runnable runnable) {
            String name = this.mPrefix + this.mThreadNum.getAndIncrement();
            Thread ret = new Thread(this.mGroup, runnable, name, 0L);
            ret.setDaemon(this.mDaemo);
            return ret;
        }

        public ThreadGroup getThreadGroup() {
            return this.mGroup;
        }
    }
}




