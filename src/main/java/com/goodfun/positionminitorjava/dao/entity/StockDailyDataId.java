package com.goodfun.positionminitorjava.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Embeddable
public class StockDailyDataId implements Serializable {

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "trade_date")
    private Date tradeDate;

}
