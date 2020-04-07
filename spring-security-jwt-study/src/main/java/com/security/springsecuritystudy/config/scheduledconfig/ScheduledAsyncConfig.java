package com.security.springsecuritystudy.config.scheduledconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * SpringTask定时任务配置类
 * @author Jjcc
 * @version 1.0.0
 * @className ScheduledAsyncConfig.java
 * @createTime 2019年10月27日 21:33:00
 */
@Configuration
public class ScheduledAsyncConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //给定时任务添加线程池
        taskRegistrar.setScheduler(scheduledTaskExecutor());
    }

    /**
     * 线程配置类
     * @title scheduledTaskExecutor
     * @author Jjcc
     * @return org.springframework.scheduling.TaskScheduler
     * @createTime 2019/10/27 22:02
     */
    @Bean
    public TaskScheduler scheduledTaskExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskExecutor = new ThreadPoolTaskScheduler();
        threadPoolTaskExecutor.setPoolSize(20);
        threadPoolTaskExecutor.setThreadNamePrefix("Schedule-Executor-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //调度器shutdown被调用时等待当前被调度的任务完成
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //等待时长
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        return threadPoolTaskExecutor;
    }
}
