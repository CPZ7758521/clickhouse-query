package com.pandora.www.clickhouse_query.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    public static ThreadPoolUtil util;

    private ThreadPoolUtil() {

    }

    public static ThreadPoolUtil getInstance() {
        if (util == null) {
            synchronized (ThreadPoolUtil.class) {
                if (util == null) {
                    System.out.println("创建实例");
                    util = new ThreadPoolUtil();
                }
            }
        }
        return util;
    }
}
