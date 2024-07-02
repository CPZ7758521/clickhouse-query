package com.pandora.www.clickhouse_query;

import com.pandora.www.clickhouse_query.bean.Deal_bid;
import com.pandora.www.clickhouse_query.bean.Quote_bid;
import com.pandora.www.clickhouse_query.utils.ThreadPoolUtils;
import lombok.SneakyThrows;
import ru.yandex.clickhouse.ClickHousePreparedStatement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class QueryApp {
    static String URL = "";
    static String USERNAME = "";
    static String PASSWORD = "";

    public static void main(String[] args) {
        HashMap<String,String[]>tableCodeMap = new HashMap<>();
        String[] quoteBidLoose = {"012282784.IB","sc_quote_date"};
        String[] dealBidLoose = {"220220.IB","transact_date"};
        String[] quoteBidstrict = {"012282233.IB","sc_quote_date"};
        String[] dealBidStrict = {"220220.IB","transact_date"};
        String[] quoteofrLoose = {"012282002.IB","sc_quote_date"};
        String[] dealofrLoose = {"220220.IB","transact_date"};
        String[] quoteofrStrict = {"012282208.IB","sc_quote_date"};
        String[] dealofrStrict = {"2228039.IB","transact_date"};
        tableCodeMap.put("v_bond_geubee_quote_bid_loose",quoteBidLoose);
        tableCodeMap.put("v_bond_geubee_deal_bid_loose",dealBidLoose);
        tableCodeMap.put("v_bond_geubee_quote_bid_strict",quoteBidstrict);
        tableCodeMap.put("v_bond_geubee_deal_bid_strict",dealBidStrict);
        tableCodeMap.put("v_bond_geubee_quote_ofr_loose",quoteofrLoose);
        tableCodeMap.put("v_bond_geubee_deal_ofr_loose",dealofrLoose);
        tableCodeMap.put("v_bond_qeubee_quote_ofr_strict",quoteofrStrict);
        tableCodeMap.put("v_bond_qeubee_deal_ofr_strict",dealofrStrict);

        try {
            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
            String sql1 = "select * from tkamc_app.";
            String sql2 = "= toDate(now()) and bond_code_wind = ?";
            ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.getInstance();

            Set<String> tables = tableCodeMap.keySet();

            for (String table : tables) {
                threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        String name = Thread.currentThread().getName();

                        try {
                            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                            long l1 = System.currentTimeMillis();
                            String sql = "";
                            ClickHousePreparedStatement statement = null;

                            ResultSet resultSet = null;

                            switch (table) {
                                case "v_bond_quebee_quote_bid_loose" :
                                    sql = sql1 + table + "where" + tableCodeMap.get(table)[1] + sql2;
                                    statement = (ClickHousePreparedStatement) connection.prepareStatement(sql);
                                    statement.setString(1, tableCodeMap.get(table)[0]);
                                    resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        Quote_bid result = quote_bid(resultSet);
                                        System.out.println(result);
                                    }

                                    resultSet.close();
                                    break;
                                case "v_bond_quebee_deal_bid_loose":
                                    sql = sql1 + table + "where" + tableCodeMap.get(table)[1] + sql2;
                                    statement = (ClickHousePreparedStatement) connection.prepareStatement(sql);
                                    statement.setString(1, tableCodeMap.get(table)[0]);
                                    resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        Deal_bid result = deal_bid(resultSet);
                                        System.out.println(result);
                                    }

                                    resultSet.close();
                                    break;

                                    //多个匹配逻辑一样，不重复写了。
                                case "v_bond_quebee_quote_bid_stritic":
                                    sql = sql1 + table + "where" + tableCodeMap.get(table)[1] + sql2;
                                    statement = (ClickHousePreparedStatement) connection.prepareStatement(sql);
                                    statement.setString(1, tableCodeMap.get(table)[0]);
                                    resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        Deal_bid result = deal_bid(resultSet);
                                        System.out.println(result);
                                    }

                                    resultSet.close();
                                    break;
                            }

                            statement.close();
                            connection.close();

                            Long l2 = System.currentTimeMillis();
                            Long l = l2 - l1;

                            //TODO 打印当前线程id 耗时
                            System.out.println("线程：" + name + "--->");
                            System.out.println("耗时：" + Double.parseDouble(l.toString()) / 1000 + "秒");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Quote_bid quote_bid(ResultSet resultSet) throws SQLException {
        Quote_bid quote_bid = new Quote_bid();
        quote_bid.setBond_code_wind(resultSet.getString(1));
        quote_bid.setTransact_date(resultSet.getDate(2));
        quote_bid.setTransact_time(resultSet.getDate(3));
        quote_bid.setSecurity_type(resultSet.getString(4));
        return quote_bid;
    }

    public static Deal_bid deal_bid(ResultSet resultSet) throws SQLException {
        Deal_bid deal_bid = new Deal_bid();
        deal_bid.setBond_code_wind(resultSet.getString(1));
        deal_bid.setTransact_date(resultSet.getDate(2));
        deal_bid.setTransact_time(resultSet.getDate(3));
        deal_bid.setSecurity_type(resultSet.getString(4));
        return deal_bid;
    }
}
