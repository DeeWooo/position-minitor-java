package com.goodfun.positionminitorjava.global;

import com.goodfun.positionminitorjava.model.RealQuote;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Quote {

    /*
    全局变量，本地缓存，保存
     */
    public static Map<String, RealQuote> realQuoteMap = new HashMap<String, RealQuote>();

    /*
    满仓金额
     */
    public static BigDecimal FULL_POSITION = new BigDecimal(50000);
}
