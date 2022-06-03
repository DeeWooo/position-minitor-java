package com.goodfun.positionminitorjava.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RealQuote {

    private String code;

    private BigDecimal realPrice;

}
