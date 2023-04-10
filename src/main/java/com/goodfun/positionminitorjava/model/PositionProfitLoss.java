package com.goodfun.positionminitorjava.model;

import com.goodfun.positionminitorjava.dao.entity.PositionEntity;

import lombok.Data;

import java.math.BigDecimal;


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
    private BigDecimal profitLossRate;
    private String profitLossRateShow;

    /*
    买入日期显示
     */
    private String buyInDateShow;

    /*
    持仓成本
     */
    private BigDecimal positionCost;

    /*
    实时持仓页面盈亏字体
     */
    private String positionTablePLStyle;

    /*
    名称
     */
    private String positionName;

    /*
    投资组合显示
     */
    private String portfolioShow;

}
