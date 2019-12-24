package com.tc.xtaskschedule.service.task.dynamicsql;

import com.tc.xtaskschedule.service.task.dynamicsql.customsql.CustomDynamicSql;
import com.tc.xtaskschedule.service.task.dynamicsql.customsql.MySqlCustomSqlImpl;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.MySqlTableSqlImpl;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.TableDynamicSql;

/**
 * Created by hbxia on 2017/4/11.
 */
public class MySqlFactory extends AbstractDynamicSqlFactory {

    @Override
    public CustomDynamicSql createCustomSql() {
        return new MySqlCustomSqlImpl();
    }

    @Override
    public TableDynamicSql createTableSql() {
        return new MySqlTableSqlImpl();
    }
}
