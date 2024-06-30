package com.pandora.www.clickhouse_query;

import com.pandora.www.clickhouse_query.utils.ThreadPoolUtil;
import lombok.SneakyThrows;

import java.util.concurrent.ThreadPoolExecutor;

public class QueryApp {
    public static void main(String[] args) {
        ThreadPoolUtil instance = ThreadPoolUtil.getInstance();
        instance = null;
        ThreadPoolUtil.util = null;

        ThreadPoolUtil instance1 = ThreadPoolUtil.getInstance();
        System.out.println("ok");
    }
}
