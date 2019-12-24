package com.tc.xtaskschedule.service.http;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static rx.Observable.just;

public class HttpLoadBalancerClient {

    private static final RestTemplate REST_CLIENT = new RestTemplateBuilder().build();
    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

    static {
        HTTP_HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    public static String call(final ILoadBalancer loadBalancer, final String path, final String body) throws Exception {

        return LoadBalancerCommand.<String>builder()
                .withLoadBalancer(loadBalancer)
                .build()
                .submit(server -> {
                    try {
                        String url = "http://" + server.getHost() + ":" + server.getPort() + path;
                        HttpEntity<String> entity = new HttpEntity<>(body, HTTP_HEADERS);
                        String response = REST_CLIENT.postForObject(url, entity, String.class);
                        return just(response);
                    } catch (Exception ex) {
                        throw ex;
                    }
                }).toBlocking().first();
    }
}
