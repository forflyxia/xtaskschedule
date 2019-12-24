package com.tc.xtaskschedule.service.task;


import com.tc.xtaskschedule.service.task.executors.ElasticSearchTask;
import com.tc.xtaskschedule.service.task.executors.HttpTask;

/**
 * Created by hbxia on 2017/3/28.
 */
public class TaskFactory {

    /**
     *
     * @param taskContext
     * @return
     */
    public static BaseTask create(TaskContext taskContext) {
        String taskExecutorType = taskContext.getTaskConfig().getTaskExecutorType();
        TaskEnum.TaskExecutorType type = TaskEnum.TaskExecutorType.getValue(taskExecutorType);
        switch (type) {
            case HTTP:
                return new HttpTask(taskContext);
            case ElasticSearch:
                return new ElasticSearchTask(taskContext);
            default:
                return new HttpTask(taskContext);
        }
    }
}
