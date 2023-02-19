package com.goodfun.positionminitorjava.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.goodfun.positionminitorjava.dao.StockDailyDataRepository;
import com.goodfun.positionminitorjava.dao.entity.StockDailyDataEntity;
import com.goodfun.positionminitorjava.dao.entity.StockDailyDataId;
import com.goodfun.positionminitorjava.model.StockDailyData;
import com.goodfun.positionminitorjava.service.api.TushareClient;
import com.goodfun.positionminitorjava.service.api.TushareResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoryQuoteService {

    @Autowired
    private TushareClient client;

    @Autowired
    private StockDailyDataRepository stockDailyDataRepository;

    public void getAndSaveStockDailyData(String stockCode, String startDate, String endDate) {

        TushareResponse response = client.getStockDailyData(stockCode,startDate,endDate);

        TushareResponse.ResponseDate datas = response.getData();
//        StockDailyData[] items = datas.getItems();

//        JSONObject jsonObject = new JSONObject(response.getData());

        Object[] items = datas.getItems();

        for (Object item : items) {

            List data = (List) item;

            String tsCode = data.get(0).toString();
            String tradeDateStr = data.get(1).toString();
            BigDecimal openPrice = new BigDecimal(data.get(2).toString());
            BigDecimal closePrice = new BigDecimal(data.get(3).toString());
            BigDecimal highPrice = new BigDecimal(data.get(4).toString());
            BigDecimal lowPrice = new BigDecimal(data.get(5).toString());
            BigDecimal volume = new BigDecimal(data.get(6).toString());

//            处理Tushare返回的日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(tradeDateStr, formatter);
            Date tradeDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


            StockDailyData stockDailyData = new StockDailyData (tsCode, tradeDate, openPrice, closePrice, highPrice, lowPrice, volume);

            StockDailyDataEntity entity = convertModel2Entity(stockDailyData);
            stockDailyDataRepository.save(entity);

        }
    }


//    public void get

    public void getAndSaveAllStockDailyData(){

    }

    private StockDailyDataEntity convertModel2Entity(StockDailyData model){
        StockDailyDataEntity entity = new StockDailyDataEntity();
        StockDailyDataId stockDailyDataId = new StockDailyDataId();
        stockDailyDataId.setStockCode(model.getStockCode());
        stockDailyDataId.setTradeDate(model.getTradeDate());

        entity.setId(stockDailyDataId);
        entity.setOpenPrice(model.getOpenPrice());
        entity.setClosePrice(model.getClosePrice());
        entity.setHighPrice(model.getHighPrice());
        entity.setLowPrice(model.getLowPrice());
        entity.setVolume(model.getVolume());

        return entity;

    }


}
