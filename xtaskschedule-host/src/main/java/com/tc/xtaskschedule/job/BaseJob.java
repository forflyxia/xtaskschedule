package com.tc.xtaskschedule.job;

import cn.tcc.foundation.core.log.TraceLogBuilder;
import com.tc.xtaskschedule.service.RunningLogPool;
import com.xxl.job.core.handler.IJobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hbxia on 2017/3/23.
 */
@Service
public abstract class BaseJob extends IJobHandler {

    @Autowired
    protected RunningLogPool runningLogPool;

    public void onAbort() {
    }

    /**
     *
     * @param taskName
     */
    protected void logStrat(String taskName) {
        TraceLogBuilder.start();
        TraceLogBuilder.appendLine("[%s]开始.", taskName);
    }

    /**
     *
     * @param taskName
     */
    protected String logEnd(String taskName) {
        TraceLogBuilder.appendLine("[%s]结束.", taskName);
        String log = TraceLogBuilder.end();
        String htmlLog = log.replace("\r\n", "<br/>");
        runningLogPool.add(taskName, htmlLog);
        return log;
    }
}
