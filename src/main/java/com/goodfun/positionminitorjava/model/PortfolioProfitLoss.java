package com.goodfun.positionminitorjava.model;

import com.goodfun.positionminitorjava.global.Portfolio;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PortfolioProfitLoss {

    /*
    同组合下标的集合
     */
    List<TargetProfitLoss> targetProfitLossList;

    /*
    投资组合
     */
    private Portfolio portfolio;

    /*
    总持仓成本
     */
    private BigDecimal sumPositionCost;

    /*
    总盈亏
     */
    private BigDecimal sumProfitLosses;

    /*
    总盈亏比
     */
    private BigDecimal sumProfitLossesRate;

    /*
    满仓金额
     */
    private BigDecimal fullPosition;

    /*
        盈亏字体样式
    */
    private String pLStyle;



}
