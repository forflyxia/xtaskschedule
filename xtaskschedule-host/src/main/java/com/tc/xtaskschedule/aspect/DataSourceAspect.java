package com.tc.xtaskschedule.aspect;

import com.tc.xtaskschedule.datasource.DataSourceType;
import com.tc.xtaskschedule.datasource.DynamicDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class DataSourceAspect {

    private static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Before("execution(* com.tc.xtaskschedule.repository.mapper.mysql.taskdb..*(..))")
    public void switchProductDbBefore(JoinPoint point) throws Throwable {
        String log = String.format("switch to %s!", DataSourceType.TASKDB);
        logger.debug(log);
        DynamicDataSource.setDataSourceKey(DataSourceType.TASKDB);
    }

    @After("execution(* com.tc.xtaskschedule.repository.mapper.mysql.taskdb..*(..))")
    public void switchProductDbAfter() throws Throwable {
        DynamicDataSource.clearDataSourceKey();
    }
}
