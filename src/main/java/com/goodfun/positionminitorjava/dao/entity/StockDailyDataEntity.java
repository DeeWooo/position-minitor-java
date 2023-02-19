package com.goodfun.positionminitorjava.dao.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "stock_daily_data")
public class StockDailyDataEntity {

    @EmbeddedId
    private StockDailyDataId id;

//    @Id
//    @Column(name = "stock_code")
//    private String stockCode;
//
//    @Id
//    @Column(name = "trade_date")
//    private Date tradeDate;

    @Column(name = "open_price")
    private BigDecimal openPrice;

    @Column(name = "close_price")
    private BigDecimal closePrice;

    @Column(name = "high_price")
    private BigDecimal highPrice;

    @Column(name = "low_price")
    private BigDecimal LowPrice;

    @Column(name = "volume")
    private BigDecimal volume;

}