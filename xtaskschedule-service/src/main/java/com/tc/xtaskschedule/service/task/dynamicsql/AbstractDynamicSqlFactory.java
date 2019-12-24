package com.tc.xtaskschedule.service.task.dynamicsql;


import com.tc.xtaskschedule.service.task.dynamicsql.customsql.CustomDynamicSql;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.TableDynamicSql;

/**
 * Created by hbxia on 2017/4/10.
 */
public abstract class AbstractDynamicSqlFactory {

    public abstract CustomDynamicSql createCustomSql();

    public abstract TableDynamicSql createTableSql();

    public static AbstractDynamicSqlFactory create(String dbType) {
        switch (dbType.toLowerCase()) {
            case "sqlserver":
                return new SqlServerFactory();
            case "mysql":
                return new MySqlFactory();
            default:
                return null;
        }
    }
}
