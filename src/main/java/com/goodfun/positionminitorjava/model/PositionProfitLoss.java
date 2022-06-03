package com.goodfun.positionminitorjava.model;

import com.goodfun.positionminitorjava.dao.entity.PositionEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.zip.DeflaterOutputStream;

@Data
public class PositionProfitLoss {

    private PositionEntity position;

    /*
    实时价格
     */
    private BigDecimal realPrice;

    /*
    盈亏
     */
    private BigDecimal profitLoss;

    /*
    盈亏比
     */
    private String profitLossRateShow;

    /*
    买入日期显示
     */
    private String buyInDateShow;

    /*
    持仓成本
     */
    private BigDecimal positionCost;

}
