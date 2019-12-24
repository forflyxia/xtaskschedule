package com.tc.xtaskschedule.web.controller;

import cn.tcc.foundation.core.serialization.FastJsonSerializer;
import com.tc.xtaskschedule.job.jobs.IncrementSyncJob;
import com.tc.xtaskschedule.service.RunningLogPool;
import com.tc.xtaskschedule.vo.TaskLog;
import com.tc.xtaskschedule.web.BaseController;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by hbxia on 2017/4/21.
 */
@Controller
public class RunningLogController extends BaseController {

    @Autowired
    private RunningLogPool logPool;

    @Autowired
    private IncrementSyncJob incrementSyncJob;

    @RequestMapping("/logs")
    public String getlogs(HttpServletRequest request, Model model){
        String taskName = request.getParameter("taskname");
        List<TaskLog> logs = logPool.getLogs(taskName);
        model.addAttribute("logs", logs);
        return "logs";
    }

    @RequestMapping("/clear")
    public String clear(){
        logPool.clear();
        return "logs";
    }

    @RequestMapping("/schedule")
    @ResponseBody
    public String schedule(HttpServletRequest request) throws Exception {
        String args = request.getParameter("args");
        String[] array = null;
        if (!StringUtils.isEmpty(args)) {
            array = args.split(",");
        }
        ReturnT returnT = incrementSyncJob.execute(array);
        return FastJsonSerializer.toString(returnT);
    }
}
