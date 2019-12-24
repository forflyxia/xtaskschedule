package com.tc.xtaskschedule.job.jobs;

import cn.tcc.foundation.core.log.TraceLogBuilder;
import com.tc.xtaskschedule.job.BaseJob;
import com.tc.xtaskschedule.selfschedule.HeartBeatContext;
import com.tc.xtaskschedule.selfschedule.TaskSelfSchedule;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.util.ShardingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hbxia on 2017/3/23.
 */
@JobHander(value = "IncrementSyncJob")
@Service
public class IncrementSyncJob extends BaseJob {

    @Autowired
    private TaskSelfSchedule taskSelfSchedulePool;

    private static final String TASK_NAME = "心跳调度";

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        ReturnT returnT = new ReturnT<>(200);
        HeartBeatContext selfScheduleContext = new HeartBeatContext();
        selfScheduleContext.args = strings;
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        if (shardingVO == null) {
            selfScheduleContext.shardings = Integer.parseInt(selfScheduleContext.args[0]);
            selfScheduleContext.myShardingIndex = Integer.parseInt(selfScheduleContext.args[1]);
        } else {
            selfScheduleContext.shardings = shardingVO.getTotal();
            selfScheduleContext.myShardingIndex = shardingVO.getIndex() + 1;
        }
        logStrat(TASK_NAME);
        TraceLogBuilder.appendLine("Shardings:%s,MyShardingIndex:%s",selfScheduleContext.shardings, selfScheduleContext.myShardingIndex);
        try {
            taskSelfSchedulePool.start(selfScheduleContext);
        } catch (Exception ex) {
            returnT.setCode(500);
            TraceLogBuilder.appendLine("%s异常，异常信息:%s", TASK_NAME, ex.getMessage());
            boolean result = taskSelfSchedulePool.cancel();
            if (result == false) {
                TraceLogBuilder.appendLine("取消失败");
            }
        } finally {
            returnT.setMsg(logEnd(TASK_NAME));
        }
        return returnT;
    }
}
