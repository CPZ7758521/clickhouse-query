package com.pandora.www.clickhouse_query.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    public static ThreadPoolExecutor pool;

    public ThreadPoolUtil() {
    }

    public static ThreadPoolExecutor getInstance() {

        if (pool == null) {
            synchronized (ThreadPoolUtil.class) {
                if (pool == null) {
                    pool = new ThreadPoolExecutor(
                            10,
                            50,
                            30L,
                            TimeUnit.SECONDS,
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE)
                    );
                }
            }
        }
        return pool;
    }
}
