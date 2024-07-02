package com.pandora.www.clickhouse_query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Quote_bid {
    String bond_code_wind;
    Date transact_date;
    Date transact_time;
    String security_type;
    BigDecimal yield;
    Long deal_cnt;
    BigDecimal bid_size_add_filter;
    String bid_size_add_filter_flag;
    BigDecimal strike_price;
    BigDecimal full_price;
}