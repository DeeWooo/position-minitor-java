package com.goodfun.positionminitorjava.service.api;

import com.goodfun.positionminitorjava.PositionMinitorJavaApplicationTests;
import com.goodfun.positionminitorjava.model.StockDailyData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class TushareClientTest extends PositionMinitorJavaApplicationTests {

    @Autowired
    private TushareClient client;

    @Test
    void getStockDailyData() {
        String tsCode = "600036.SH";
        String startDate = "20230101";
        String endDate = "20230219";


        TushareResponse response = client.getStockDailyData(tsCode,startDate,endDate);

        response.getData().getItems();


    }
}