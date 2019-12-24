package com.tc.xtaskschedule.repository;

import com.tc.xtaskschedule.repository.mapper.mysql.taskdb.TaskMapper;
import com.tc.xtaskschedule.repository.mapper.mysql.taskdb.TaskTodoItemsMapper;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskTodoItemsPo;
import com.tc.xtaskschedule.service.task.TaskConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbxia on 2017/4/18.
 */
@Repository
public class TaskTodoItemsRepository {

    @Autowired
    private TaskTodoItemsMapper todoItemsMapper;

    public boolean backupTaskTodoItems() {
        todoItemsMapper.backupTaskTodoItems();
        return true;
    }
}
