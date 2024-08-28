package com.marsol.sync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Value("${scale.network.thread.pool.size}")
    private int scaleNetworkThreadPoolSize;
    @Value("${data.processing.thread.pool.size}")
    private int dataProcessingThreadPoolSize;

    @Bean
    public ThreadPoolTaskScheduler scaleNetThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(scaleNetworkThreadPoolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("scaleNetThreadPoolTaskScheduler");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    @Bean
    public ThreadPoolTaskScheduler dataProcessingThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(dataProcessingThreadPoolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("dataProcessingThreadPoolTaskScheduler");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }


}
