package com.tc.xtaskschedule;

import cn.tcc.foundation.core.ProfilesConfig;
import cn.tcc.foundation.es.EsInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.servlet.ServletException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        JmxAutoConfiguration.class,
        WebServicesAutoConfiguration.class,
        IntegrationAutoConfiguration.class,
        WebSocketAutoConfiguration.class,
        GroovyTemplateAutoConfiguration.class},
        scanBasePackages = {"com.tc.xtaskschedule"})
public class XTaskScheduleApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(XTaskScheduleApplication.class);
    }

    public static void main(String[] args) {
        new ProfilesConfig().getProfiles();
        EsInitializer initializer = new EsInitializer();
        try {
            initializer.onStartup();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        SpringApplication.run(XTaskScheduleApplication.class, args);
        System.out.println("=================XTaskScheduleApplication Started!!!================");
    }
}
