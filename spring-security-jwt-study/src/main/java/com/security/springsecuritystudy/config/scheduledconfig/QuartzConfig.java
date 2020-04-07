package com.security.springsecuritystudy.config.scheduledconfig;

/**
 * quartz定时任务配置配
 * @author Jjcc
 * @version 1.0.0
 * @className QuartzConfig.java
 * @createTime 2019年10月30日 10:19:00
 */
//@Slf4j
//@Configuration
public class QuartzConfig {

//    /**
//     * JobDetail指定任务类
//     * @title testQuartz1
//     * @author Jjcc
//     * @return org.quartz.JobDetail
//     * @createTime 2019/10/30 11:31
//     */
//    @Bean
//    public JobDetail simpleTaskJobDetail() {
//        return JobBuilder.newJob(QuartzSimpleTask.class).withIdentity("simpleTaskJobDetail").storeDurably().build();
//    }
//
//    /**
//     * Simple触发器定义与设置
//     * 一个trigger只对应一个job，schedule调度trigger执行对应的job
//     * @title simpleTaskTrigger
//     * @author Jjcc
//     * @return org.quartz.Trigger
//     * @createTime 2019/10/30 11:40
//     */
//    @Bean
//    public Trigger simpleTaskTrigger() {
//        //Simple类型：可设置时间间隔、是否重复、触发频率（misfire机制）等
//        //每隔5秒执行一次
//        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
//
//        return TriggerBuilder.newTrigger().forJob(simpleTaskJobDetail()).
//                withIdentity("simpleTaskTrigger").withSchedule(simpleScheduleBuilder).build();
//    }
//
//    /**
//     * JobDetail指定工作类
//     * @title cronTaskJobDetail
//     * @author Jjcc
//     * @return org.quartz.JobDetail
//     * @createTime 2019/10/30 21:42
//     */
//    @Bean
//    public JobDetail cronTaskJobDetail() {
//        return JobBuilder.newJob(QuartzCronTask.class).withIdentity("cronTaskJobDetail").storeDurably().build();
//    }
//
//    /**
//     * cron触发器的定义与配置
//     * @title cronTaskTrigger
//     * @author Jjcc
//     * @return org.quartz.Trigger
//     * @createTime 2019/10/30 21:53
//     */
//    @Bean
//    public Trigger cronTaskTrigger() {
//        //cron类型的触发器
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?").withMisfireHandlingInstructionDoNothing();
//
//        return TriggerBuilder.newTrigger().forJob(cronTaskJobDetail())
//                .withIdentity("cronTaskTrigger")
//                .withSchedule(cronScheduleBuilder)
//                .withDescription("corn类型的触发器")
//                .startNow()
//                .build();
//    }

}
