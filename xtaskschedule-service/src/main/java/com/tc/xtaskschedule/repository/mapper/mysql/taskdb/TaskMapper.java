package com.tc.xtaskschedule.repository.mapper.mysql.taskdb;

import java.util.List;
import java.util.Map;

import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskTodoItemsPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by hbxia on 2017/3/27.
 */
@Repository
public interface TaskMapper {

    List<TaskPo> getTasks();

    TaskIncrementPo getTaskIncrement(Integer taskId);

    int getTaskTodoItemsCount(Integer taskId);

    List<TaskTodoItemsPo> getTaskTodoItems(@Param("taskId") Integer taskId, @Param("shardingNo")Integer shardingNo);

    boolean insertTaskIncrement(TaskIncrementPo item);

    boolean updateTaskIncrement(TaskIncrementPo item);

    boolean batchInsertTaskTodoItems(List<TaskTodoItemsPo> items);

    boolean updateTaskTodoItemsStatus(@Param("id")Integer id, @Param("status")Integer status);

    boolean updateTaskTodoItemsRemark(@Param("id")Integer id, @Param("remark")String remark);
}
