package com.tc.xtaskschedule.service.task.executors;

import cn.tcc.foundation.core.convert.StringTo;
import cn.tcc.foundation.core.serialization.FastJsonSerializer;
import cn.tcc.foundation.es.search.bulk.EsBulkClient;
import cn.tcc.foundation.es.search.reponse.EsBulkResponse;
import cn.tcc.foundation.es.search.reponse.EsResponse;
import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.service.task.*;
import com.tc.xtaskschedule.service.task.dynamicsql.AbstractDynamicSqlFactory;
import com.tc.xtaskschedule.service.task.dynamicsql.customsql.CustomDynamicSql;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.TableDynamicSql;
import com.tc.xtaskschedule.vo.DataSyncVo;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by hbxia on 2017/3/23.
 */
public class ElasticSearchTask extends BaseTask {

    public ElasticSearchTask(TaskContext context) {
        super(context);
    }

    @Override
    public DataSyncVo loadData(TaskIncrementPo taskIncrementPo) {
        TaskConfig taskConfig = this.getTaskConfig();
        List<DataSyncPo> items = new ArrayList<>();
        AbstractDynamicSqlFactory factory = AbstractDynamicSqlFactory.create(taskConfig.getDbType());
        if (taskConfig.getSqlExecutorType().equalsIgnoreCase("customsql")) {
            CustomDynamicSql customSql = factory.createCustomSql();
            items = customSql.getFullEqualsItems(taskConfig, taskIncrementPo);
            List<DataSyncPo> greaterItems = customSql.getFullGreaterItems(taskConfig,taskIncrementPo, items.size());
            items.addAll(greaterItems);
        }
        if (taskConfig.getSqlExecutorType().equalsIgnoreCase("tablesql")) {
            TableDynamicSql tableSql = factory.createTableSql();
            items = tableSql.getFullEqualsItems(taskConfig, taskIncrementPo);
            List<DataSyncPo> greaterItems = tableSql.getFullGreaterItems(taskConfig, taskIncrementPo,items.size());
            items.addAll(greaterItems);
        }
        return new DataSyncVo(items);
    }

    @Override
    public Callable<BatchTaskContext> execute(BatchTaskContext batchTaskcontext) {
        return new InternalTaskExecutor(batchTaskcontext);
    }

    class InternalTaskExecutor implements Callable<BatchTaskContext> {

        private BatchTaskContext batchTaskContext;

        public InternalTaskExecutor(BatchTaskContext context) {
            this.batchTaskContext = context;
        }

        @Override
        public BatchTaskContext call() throws Exception {
            List<DataSyncPo> failedItems = new ArrayList<>();

            Map<String, String> map = new HashMap<>();
            for (DataSyncPo item : batchTaskContext.getBatchItems()) {
                try {
                    if (batchTaskContext.getTask().getStatus() == TaskEnum.TaskStatus.STOPPED){
                        failedItems.add(item);
                        continue;
                    }
                    if (!map.containsKey(item.getItemKey())) {
                        map.put(item.getItemKey(), item.getExtendData());
                    }
                } catch (Exception ex) {
                    failedItems.add(item);
                }
            }

            if (map.size() > 0) {
                String esAddress = batchTaskContext.getTask().getTaskConfig().getDestinationAddress();
                List<String> address = StringTo.toList(esAddress, ":");
                if (address.size() != 3) {
                    throw new Exception(String.format("elasticsearch task %s destination address invalid.", batchTaskContext.getTask().getTaskName()));
                }

                String clusterName = address.get(0);
                String indexName = address.get(1);
                String typeName = address.get(2);
                EsBulkResponse<String> response = (EsBulkResponse<String>)new EsBulkClient<String>(clusterName).from(indexName, typeName).source(map).execute(String.class);
                if (response != null) {
                    for (DataSyncPo item : batchTaskContext.getBatchItems()) {
                        if (response.getSuccessItemsMap().containsKey(item.getItemKey())) {
                            taskRepository.updateTaskTodoItemsStatus(item.getId(), 0);
                        } else {
                            failedItems.add(item);
                        }
                    }
                }
            }
            batchTaskContext.setFailedBatchItems(failedItems);
            return this.batchTaskContext;
        }
    }
}
