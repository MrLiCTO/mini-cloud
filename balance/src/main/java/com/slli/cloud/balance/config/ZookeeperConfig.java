package com.slli.cloud.balance.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Mr_Li on 2017/6/7.
 */
@Configuration
public class ZookeeperConfig {
    private final static String CONNECT_ADDR="localhost:2181";
    private final static int SESSION_OUTTIME=5000;
    @Bean
    public CuratorFramework curatorFramework(){
        //1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2 通过工厂创建连接
        CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECT_ADDR,retryPolicy);
        client.start();
        return client;
    }
}
