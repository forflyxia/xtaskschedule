package com.tc.xtaskschedule.job.jobs;

import com.tc.xtaskschedule.selfschedule.TaskSelfSchedule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by hbxia on 2017/4/21.
 */
@ContextConfiguration(value = {"classpath*:/application.yml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class IncrementSyncJobTest_SqlServer_TableSql {

    @Resource(name="taskSelfSchedulePool")
    TaskSelfSchedule taskSelfSchedulePool = null;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onStart() throws Exception {
        System.out.print("IncrementSyncJobTest_SqlServer_TableSql BEGIN.");

        int taskCount = 1;

        taskSelfSchedulePool.start(null);

        System.out.print("IncrementSyncJobTest_SqlServer_TableSql END.");
    }

}