package com.goodfun.positionminitorjava.service;

import com.goodfun.positionminitorjava.PositionMinitorJavaApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Access;

import static org.junit.jupiter.api.Assertions.*;

class HistoryQuoteServiceTest extends PositionMinitorJavaApplicationTests {

    @Autowired
    private HistoryQuoteService historyQuoteService;

    @Test
    void getAndSaveStockDailyData() {
        String stockCode = "600036.SH";
        String startDate = "20230101";
        String endDate = "20230219";

        historyQuoteService.getAndSaveStockDailyData(stockCode,startDate,endDate);
    }
}