package com.tc.xtaskschedule.service.task.dynamicsql.tablesql;

import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.service.task.TaskConfig;

public class TableSqlWrapper {

	private String dbType;
	private int topN;
	private String tableName;
	private String keyColumnName;
	private String keyColumnType;
	private String maxKey_LastTime;
	private String maxTime_LastTime;

	public TableSqlWrapper(TaskConfig taskConfig, TaskIncrementPo taskIncrement, int topN) {
		this.dbType = taskConfig.getDbType();
		this.topN = topN;
		this.tableName = taskConfig.getTableName();
		this.keyColumnName = taskConfig.getKeyColumnName();
		this.keyColumnType= taskConfig.getKeyColumnType();
		this.maxKey_LastTime = taskIncrement.getMaxKey_LastTime();
		this.maxTime_LastTime = taskIncrement.getMaxTime_LastTime();
	}

	public String getDbType() {
		return dbType.toLowerCase();
	}

	public int getTopN() {
		return topN;
	}

	public String getTableName() {
		return tableName;
	}

	public String getKeyColumnType() {
		return keyColumnType;
	}

	public String getMaxKey_LastTime() {
		return maxKey_LastTime;
	}

	public String getMaxTime_LastTime() {
		return maxTime_LastTime;
	}

	public String getKeyColumnName() {
		return keyColumnName;
	}
}
