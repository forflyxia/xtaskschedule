package com.tc.xtaskschedule.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourcesConfig {

    @Bean(name = DataSourceType.TASKDB)
    @ConfigurationProperties(prefix = "spring.datasource.taskdb.master")
    public DataSource taskdb() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = DataSourceType.PRODUCTDB)
    @ConfigurationProperties(prefix = "spring.datasource.productdb.master")
    public DataSource productdb() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = DataSourceType.ORDERDB)
    @ConfigurationProperties(prefix = "spring.datasource.orderdb.master")
    public DataSource orderdb() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = DataSourceType.SEARCHDB)
    @ConfigurationProperties(prefix = "spring.datasource.searchdb.master")
    public DataSource searchdb() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = DataSourceType.MESSAGEDB)
    @ConfigurationProperties(prefix = "spring.datasource.messagedb.master")
    public DataSource messagedb() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", "thomascook");
        reg.addInitParameter("loginPassword", "thomascook");
        //禁用HTML页面上的ResetAll功能
        reg.addInitParameter("resetEnable", "true");
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");

        return filterRegistrationBean;
    }

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     *
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dynamicdb() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(taskdb());

        Map<Object, Object> datasourceMap = new HashMap();
        datasourceMap.put(DataSourceType.TASKDB, taskdb());
        datasourceMap.put(DataSourceType.PRODUCTDB, productdb());
        datasourceMap.put(DataSourceType.ORDERDB, orderdb());
        datasourceMap.put(DataSourceType.SEARCHDB, searchdb());
        datasourceMap.put(DataSourceType.MESSAGEDB, messagedb());
        dynamicDataSource.setTargetDataSources(datasourceMap);
        return dynamicDataSource;
    }
}
