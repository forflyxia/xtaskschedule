package com.tc.xtaskschedule.service.http;

import com.netflix.loadbalancer.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuxingye
 */
public class LoadBalancerProvider {
    private static final ConcurrentHashMap<Integer, ILoadBalancer> LOAD_BALANCER_MAP = new ConcurrentHashMap<>();

    public static ILoadBalancer getLoadBalancer(Integer taskId, List<Server> servers) {
        ILoadBalancer loadBalancer = LOAD_BALANCER_MAP.get(taskId);
        if (loadBalancer == null ||
                loadBalancer.getAllServers() == null || servers == null ||
                loadBalancer.getAllServers().hashCode() != servers.hashCode()) {
            addLoadBalancer(taskId, servers);
        }
        return LOAD_BALANCER_MAP.get(taskId);
    }

    private static boolean addLoadBalancer(Integer taskId, List<Server> servers) {
        LOAD_BALANCER_MAP.put(taskId, LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(servers));
        return true;
    }

    public LoadBalancerStats getLoadBalancerStats(Integer taskId) {
        return ((BaseLoadBalancer) LOAD_BALANCER_MAP.get(taskId)).getLoadBalancerStats();
    }
}
