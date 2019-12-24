package com.tc.xtaskschedule.service.task.dynamicsql;

import com.tc.xtaskschedule.service.task.dynamicsql.customsql.CustomDynamicSql;
import com.tc.xtaskschedule.service.task.dynamicsql.customsql.SqlServerCustomSqlImpl;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.SqlServerTableSqlImpl;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.TableDynamicSql;

/**
 * Created by hbxia on 2017/4/11.
 */
public class SqlServerFactory extends AbstractDynamicSqlFactory {

    @Override
    public CustomDynamicSql createCustomSql() {
        return new SqlServerCustomSqlImpl();
    }

    @Override
    public TableDynamicSql createTableSql() {
        return new SqlServerTableSqlImpl();
    }
}
