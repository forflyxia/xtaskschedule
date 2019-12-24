package com.tc.xtaskschedule.repository.mapper.mysql.taskdb;

import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskTodoItemsPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hbxia on 2017/3/27.
 */
@Repository
public interface TaskTodoItemsMapper {

    int backupTaskTodoItems();
}
