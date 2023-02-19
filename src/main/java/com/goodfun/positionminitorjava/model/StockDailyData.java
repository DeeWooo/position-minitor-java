package com.goodfun.positionminitorjava.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockDailyData {

    private String stockCode;

    private Date tradeDate;

    private BigDecimal openPrice;

    private BigDecimal closePrice;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private BigDecimal volume;

    public StockDailyData(String tsCode, Date tradeDate, BigDecimal openPrice, BigDecimal closePrice, BigDecimal highPrice, BigDecimal lowPrice, BigDecimal volume) {

        this.stockCode = tsCode;
        this.tradeDate = tradeDate;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
    }
}
