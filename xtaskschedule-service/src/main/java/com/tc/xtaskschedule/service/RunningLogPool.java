package com.tc.xtaskschedule.service;

import com.tc.xtaskschedule.vo.TaskLog;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hbxia on 2017/4/24.
 */
@Service
public class RunningLogPool {

    private volatile Integer index = 0;
    private static final List<TaskLog> logs = new ArrayList<>();

    public void add(String taskName, String log) {

        if (index > 1000) {
            logs.clear();
            index = 0;
        }
        TaskLog item = new TaskLog();
        item.setTaskName(taskName);
        item.setLog(log);
        logs.add(item);
        index++;
    }

    public List<TaskLog> getLogs(String taskName) {
        if (taskName == null || taskName.isEmpty()) {
            return logs;
        } else {
            return logs.stream().filter(p->p.getTaskName().equalsIgnoreCase(taskName)).collect(Collectors.toList());
        }
    }

    public boolean clear() {
        logs.clear();
        return true;
    }
}
