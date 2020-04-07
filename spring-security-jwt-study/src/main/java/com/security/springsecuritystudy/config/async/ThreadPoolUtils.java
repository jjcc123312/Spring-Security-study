package com.security.springsecuritystudy.config.async;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className ThreadPoolUtils.java
 * @createTime 2019年10月14日 14:32:00
 */
public class ThreadPoolUtils {

    private ExecutorService pool;

    private ThreadPoolUtils() {
        if (null != ThreadPoolUtilsInnerClass.THREAD_POOL) {
            throw new RuntimeException("不能手动创建实例对象！");
        }
        init();
    }

    private static class ThreadPoolUtilsInnerClass {
        private final static ThreadPoolUtils THREAD_POOL = new ThreadPoolUtils();
    }

    public static ThreadPoolUtils getThreadPool() {
        return ThreadPoolUtilsInnerClass.THREAD_POOL;
    }

    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小----3
     * maximumPoolSize 最大线程池大小----6
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----20+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(10)====10容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,
     * 							即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
     * 						          任务会交给RejectedExecutionHandler来处理
     */
    private void init() {
        pool = new ThreadPoolExecutor(3,10,20,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(30),
                new CustomThreadFactory(), new CustomRejectedExecutionHandler());


    }

    /**
     * 销毁线程池，运行中的线程不会被销毁
     * @title destory
     * @author Jjcc
     * @return void
     * @createTime 2019/10/14 15:38
     */
    public void destory() {
        if (null != pool) {
            pool.shutdown();
        }
    }

    /**
     * 获取线程池
     * @title getCustomThreadPoolExecutor
     * @author Jjcc
     * @return java.util.concurrent.ExecutorService
     * @createTime 2019/10/14 15:38
     */
    public ExecutorService getCustomThreadPoolExecutor() {
        return this.pool;
    }

    private class CustomThreadFactory implements ThreadFactory {

        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName =  ThreadPoolUtils.class.getSimpleName()+count.addAndGet(1);

            System.out.println(threadName);
            t.setName(threadName);
            return t;
        }
    }

    private class CustomRejectedExecutionHandler implements  RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //记录异常
            System.out.println("error...................");
        }
    }
}


