package com.tc.xtaskschedule.service.task;

/**
 * Created by hbxia on 2017/3/23.
 */
public class TaskConfig {

    private int taskId;
    private String taskName;
    private int nThreadsPerNode;
    private String taskArgs;
    private int rowsPerTime;
    private String dbType;
    private String sqlExecutorType;
    private String dbName;
    private String tableName;
    private String keyColumnName;
    private String keyColumnType;
    private String customSqlEquals;
    private String customSqlGreater;
    private String taskExecutorType;
    private String destinationAddress;
    private int failedRetryTimes;
    private String hostNodes;
    private int interval;
    private boolean isDownload;
    private boolean isLoop;
    private int version;


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getNThreadsPerNode() { return nThreadsPerNode;}

    public void setNThreadsPerNode(int nThreadsPerNode) {
        this.nThreadsPerNode = nThreadsPerNode;
    }

    public String getTaskArgs() {
        return taskArgs;
    }

    public void setTaskArgs(String taskArgs) {
        this.taskArgs = taskArgs;
    }

    public int getRowsPerTime() {
        return rowsPerTime;
    }

    public void setRowsPerTime(int rowsPerTime) {
        this.rowsPerTime = rowsPerTime;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getSqlExecutorType() {
        return sqlExecutorType;
    }

    public void setSqlExecutorType(String sqlExecutorType) {
        this.sqlExecutorType = sqlExecutorType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyColumnName() {
        return keyColumnName;
    }

    public void setKeyColumnName(String keyColumnName) {
        this.keyColumnName = keyColumnName;
    }

    public String getKeyColumnType() {
        return keyColumnType;
    }

    public void setKeyColumnType(String keyColumnType) {
        this.keyColumnType = keyColumnType;
    }

    public String getCustomSqlEquals() {
        return customSqlEquals;
    }

    public void setCustomSqlEquals(String customSqlEquals) {
        this.customSqlEquals = customSqlEquals;
    }

    public String getCustomSqlGreater() {
        return customSqlGreater;
    }

    public void setCustomSqlGreater(String customSqlGreater) {
        this.customSqlGreater = customSqlGreater;
    }

    public String getTaskExecutorType() {
        return taskExecutorType;
    }

    public void setTaskExecutorType(String taskExecutorType) {
        this.taskExecutorType = taskExecutorType;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public int getFailedRetryTimes() {
        return failedRetryTimes;
    }

    public void setFailedRetryTimes(int failedRetryTimes) {
        this.failedRetryTimes = failedRetryTimes;
    }

    public String getHostNodes() {
        return hostNodes;
    }

    public void setHostNodes(String hostNodes) {
        this.hostNodes = hostNodes;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }
}
