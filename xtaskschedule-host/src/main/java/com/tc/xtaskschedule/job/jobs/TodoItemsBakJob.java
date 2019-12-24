package com.tc.xtaskschedule.job.jobs;

import com.tc.xtaskschedule.job.BaseJob;
import com.tc.xtaskschedule.repository.TaskTodoItemsRepository;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hbxia on 2017/3/23.
 */
@JobHander(value = "TodoItemsBakJob")
@Service
public class TodoItemsBakJob extends BaseJob {

    @Autowired
    private TaskTodoItemsRepository taskTodoItemsRepository;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        ReturnT returnT = new ReturnT<>(200);

        try {
            taskTodoItemsRepository.backupTaskTodoItems();
        } catch (Exception ex) {
            returnT.setCode(500);

        } finally {

        }
        return returnT;
    }
}
