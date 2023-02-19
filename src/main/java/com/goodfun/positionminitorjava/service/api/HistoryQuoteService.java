package com.goodfun.positionminitorjava.service.api;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.goodfun.positionminitorjava.dao.StockDailyDataRepository;
import com.goodfun.positionminitorjava.dao.entity.StockDailyDataEntity;
import com.goodfun.positionminitorjava.dao.entity.StockDailyDataId;
import com.goodfun.positionminitorjava.model.StockDailyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class HistoryQuoteService {

    private String apiKey = "9897de9903a7ce04fa4b85bab5e531b6033d63e197faa87b7de5317f";

//    // 配置 RestTemplate
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    // 获取 Tushare API 历史行情数据并保存到 MySQL 数据库
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockDailyDataRepository stockDailyDataRepository;

    public void getAndSaveStockDailyData(String stockCode, String startDate, String endDate) {
        String url = String.format("https://api.tushare.pro/openapi/v1/stock_daily?api_key=%s&ts_code=%s&start_date=%s&end_date=%s",
                apiKey, stockCode, startDate, endDate);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray dataArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject data = dataArray.getJSONObject(i);
            String tsCode = data.getStr("ts_code");
            String tradeDateStr = data.getStr("trade_date");
            BigDecimal openPrice = data.getBigDecimal("open");
            BigDecimal closePrice = data.getBigDecimal("close");
            BigDecimal highPrice = data.getBigDecimal("high");
            BigDecimal lowPrice = data.getBigDecimal("low");
            BigDecimal volume = data.getBigDecimal("vol");


//            处理Tushare返回的日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(tradeDateStr, formatter);
            Date tradeDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


            StockDailyData stockDailyData = new StockDailyData (tsCode, tradeDate, openPrice, closePrice, highPrice, lowPrice, volume);

            StockDailyDataEntity entity = convertModel2Entity(stockDailyData);
            stockDailyDataRepository.save(entity);
        }
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
