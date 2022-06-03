package com.goodfun.positionminitorjava.global;

import com.goodfun.positionminitorjava.model.RealQuote;

import java.util.HashMap;
import java.util.Map;

public class Quote {

    /*
    全局变量，本地缓存，保存
     */
    public static Map<String, RealQuote> realQuoteMap = new HashMap<String, RealQuote>();
}
