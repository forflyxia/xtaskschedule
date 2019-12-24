package com.tc.xtaskschedule.service.task.executors;

import cn.tcc.foundation.core.serialization.FastJsonSerializer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.tc.xtaskschedule.service.http.HttpLoadBalancerClient;
import com.tc.xtaskschedule.service.http.LoadBalancerProvider;
import com.tc.xtaskschedule.repository.po.mysql.DataSyncPo;
import com.tc.xtaskschedule.repository.po.mysql.taskdb.TaskIncrementPo;
import com.tc.xtaskschedule.service.task.*;
import com.tc.xtaskschedule.service.task.dynamicsql.AbstractDynamicSqlFactory;
import com.tc.xtaskschedule.service.task.dynamicsql.customsql.CustomDynamicSql;
import com.tc.xtaskschedule.service.task.dynamicsql.tablesql.TableDynamicSql;
import com.tc.xtaskschedule.service.task.executors.contract.AckCodeType;
import com.tc.xtaskschedule.service.task.executors.contract.NotifyRequest;
import com.tc.xtaskschedule.service.task.executors.contract.NotifyResponse;
import com.tc.xtaskschedule.vo.DataSyncVo;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by hbxia on 2017/3/23.
 */
public class HttpTask extends BaseTask {

    public HttpTask(TaskContext context) {
        super(context);
    }

    @Override
    public DataSyncVo loadData(TaskIncrementPo taskIncrementPo) {
        TaskConfig taskConfig = this.getTaskConfig();
        List<DataSyncPo> items = new ArrayList<>();
        AbstractDynamicSqlFactory factory = AbstractDynamicSqlFactory.create(taskConfig.getDbType());
        if (taskConfig.getSqlExecutorType().equalsIgnoreCase("customsql")) {
            CustomDynamicSql customSql = factory.createCustomSql();
            items = customSql.getEqualsItems(taskConfig, taskIncrementPo);
            List<DataSyncPo> greaterItems = customSql.getGreaterItems(taskConfig,taskIncrementPo, items.size());
            items.addAll(greaterItems);
        }
        if (taskConfig.getSqlExecutorType().equalsIgnoreCase("tablesql")) {
            TableDynamicSql tableSql = factory.createTableSql();
            items = tableSql.getEqualsItems(taskConfig, taskIncrementPo);
            List<DataSyncPo> greaterItems = tableSql.getGreaterItems(taskConfig, taskIncrementPo,items.size());
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
            for (DataSyncPo item : batchTaskContext.getBatchItems()) {
                try {
                    if (batchTaskContext.getTask().getStatus() == TaskEnum.TaskStatus.STOPPED){
                        failedItems.add(item);
                        continue;
                    }

                    String address = batchTaskContext.getTask().getTaskConfig().getDestinationAddress();
                    int taskId = batchTaskContext.getTask().getTaskConfig().getTaskId();
                    if (StringUtils.isEmpty(address)) {
                        continue;
                    }
                    String[] urls = address.split(",");
                    List<Server> servers = new ArrayList<>();
                    List<String> paths = new ArrayList<>();
                    for (String url : urls) {
                        URL u = new URL(url);
                        servers.add(new Server(u.getHost(),u.getPort()));
                        paths.add(u.getPath());
                    }

                    if (servers.size() > 0 && paths.size() > 0) {
                        NotifyRequest request = new NotifyRequest();
                        request.setTaskName(batchTaskContext.getTask().getTaskName());
                        request.setItemKey(item.getItemKey());
                        request.setTimestamp(item.getDataChange_LastTime().toString());
                        String requestJson = FastJsonSerializer.toString(request);
                        ILoadBalancer loadBalancer = LoadBalancerProvider.getLoadBalancer(taskId, servers);
                        String text = HttpLoadBalancerClient.call(loadBalancer, paths.get(0), requestJson);
                        NotifyResponse response = FastJsonSerializer.toObject(text, NotifyResponse.class);
                        if (response != null && response.getResponseStatus().getAck() == AckCodeType.SUCCESS) {
                            taskRepository.updateTaskTodoItemsStatus(item.getId(), 0);
                        } else {
                            String responseJson = FastJsonSerializer.toString(response);
                            if (responseJson != null && responseJson.length() > 450) {
                                responseJson = responseJson.substring(0,450);
                            }
                            String message = String.format("Remote response json: %s", responseJson);
                            taskRepository.updateTaskTodoItemsRemark(item.getId(), message);
                        }
                    }
                } catch (Exception ex) {
                    String message = String.format("Exception: %s", ex.getMessage());
                    taskRepository.updateTaskTodoItemsRemark(item.getId(), message);
                    failedItems.add(item);
                }
            }
            batchTaskContext.setFailedBatchItems(failedItems);
            return this.batchTaskContext;
        }
    }
}
