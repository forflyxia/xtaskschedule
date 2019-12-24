package com.tc.xtaskschedule.job.jobs;

import com.tc.xtaskschedule.service.TaskProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hbxia on 2017/4/21.
 */
@ContextConfiguration(value = {"classpath*:/spring/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class IncrementSyncJobTest_SqlServer_CustomSql {

    @Resource(name="taskProvider")
    TaskProvider taskPool = null;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onStart() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("AAA", "AAAValue");
        map.put("aaa", "aaaValue");
        System.out.print(map.get("AAA"));
    }

}