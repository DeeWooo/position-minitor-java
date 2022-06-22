package com.goodfun.positionminitorjava.model;

import com.goodfun.positionminitorjava.dao.entity.PositionEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TargetProfitLoss {

    /*
    持仓记录
     */
    List<PositionProfitLoss> positionProfitLosses;

    /*
    标的代码
     */
    private String code;
    /*
    标的名称
     */
    private String name;
    /*
    实时价格
     */
    private BigDecimal realPrice;

    /*
    当前仓位
     */
    private BigDecimal currentPositionRate;

    /*
    成本仓位
     */
    private BigDecimal costPositionRate;

    /*
    标的盈亏
     */
    private BigDecimal targetProfitLoss;

    /*
    标的盈亏比
     */
    private BigDecimal targetProfitLossRate;


}
