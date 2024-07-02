package com.pandora.www.clickhouse_query.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadPoolUtils {

    private static Logger LOG = LoggerFactory.getLogger(ThreadPoolUtils.class);

    private static ThreadPoolExecutor threadPool;
    private ThreadPoolUtils() {
    }

    /**
     * 无返回值，直接执行
     * @param runnable 需要运行的任务
     */
    public static void execute(Runnable runnable) {
        getInstance().execute(runnable);
    }


    /**
     * 有返回值执行
     * 主线程中使用Future.get()获取返回值是，会阻塞主线程，知道任务执行完成
     * @param callable 需要运行的任务
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getInstance().submit(callable);
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
