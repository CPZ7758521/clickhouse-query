package com.pandora.www.clickhouse_query.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class SingletonThreadPool extends ThreadPoolExecutor {

    private static Logger LOG = LoggerFactory.getLogger(SingletonThreadPool.class);


    private static SingletonThreadPool pool;

    private SingletonThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static SingletonThreadPool getInstance() {
        if (pool == null) {
            synchronized (SingletonThreadPool.class) {
                if (pool == null) {
                    pool = new SingletonThreadPool(
                            10,
                            50,
                            30L,
                            TimeUnit.SECONDS,
                            new ArrayBlockingQueue<>(20),
                            new ThreadFactoryBuilder().setNameFormat("ClickHouse Thread Pool" + "-%d").build(),
                            new ThreadPoolExecutor.AbortPolicy() {
                                @Override
                                public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                                    LOG.warn("线程池爆了，当前运行线程总数：{},活动线程数：{}。等待队列已满，等待队列等待运行任务数：{}",
                                            e.getPoolSize(),
                                            e.getActiveCount(),
                                            e.getQueue().size());
                                }
                            }
                    );
                }
            }
        }
        return pool;
    }

}
