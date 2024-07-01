package com.pandora.www.clickhouse_query.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {

    private static Logger LOG = LoggerFactory.getLogger(ThreadPoolUtils.class);

    private static ThreadPoolExecutor threadPool;
    private ThreadPoolUtils() {
    }

    public static ThreadPoolExecutor getInstance() {
        if (threadPool == null) {
            synchronized (ThreadPoolUtils.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPoolExecutor(
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
        return threadPool;
    }
}
