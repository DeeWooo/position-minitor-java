package com.goodfun.positionminitorjava.service;

import com.goodfun.positionminitorjava.dao.StockDailyDataRepository;
import com.goodfun.positionminitorjava.dao.entity.StockDailyDataEntity;
import com.goodfun.positionminitorjava.dao.entity.StockDailyDataId;
import com.goodfun.positionminitorjava.model.StockDailyData;
import com.goodfun.positionminitorjava.service.api.TushareClient;
import com.goodfun.positionminitorjava.service.api.TushareResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class HistoryQuoteService {

    private static final Logger logger = LoggerFactory.getLogger(HistoryQuoteService.class);


    @Autowired
    private TushareClient client;

    @Value("${isDataInitialized}")
    private boolean isDataInitialized;

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
            try {
                stockDailyDataRepository.save(entity);
            }
            catch (Exception e){

                System.out.println(e.toString());

                continue;

            }

        }
    }


    public void getAndSaveStockDailyData(String tradeDateStr){
        TushareResponse response = client.getStockDailyData(tradeDateStr);

        TushareResponse.ResponseDate datas = response.getData();

        Object[] items = datas.getItems();

        for (Object item : items) {

            List data = (List) item;

            String tsCode = data.get(0).toString();
            BigDecimal openPrice = new BigDecimal(data.get(2).toString());
            BigDecimal closePrice = new BigDecimal(data.get(3).toString());
            BigDecimal highPrice = new BigDecimal(data.get(4).toString());
            BigDecimal lowPrice = new BigDecimal(data.get(5).toString());
            BigDecimal volume = new BigDecimal(data.get(6).toString());


//            处理String的日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(tradeDateStr, formatter);
            Date tradeDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            StockDailyData stockDailyData = new StockDailyData (tsCode, tradeDate, openPrice, closePrice, highPrice, lowPrice, volume);

            StockDailyDataEntity entity = convertModel2Entity(stockDailyData);
            try {
                stockDailyDataRepository.save(entity);
            }
            catch (Exception e){

                System.out.println(e.toString());

                continue;

            }

        }
    }

    /**
     * 拉取近一月的数据
     */
    public void getAndSaveAllStockDailyData(){

        LocalDate now = LocalDate.now();
        LocalDate oneYearAgo = now.minusYears(1);
        LocalDate oneMonthAgo = now.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //一年前
//        String startDate = oneYearAgo.format(formatter);
//        一月前
        String startDate = oneMonthAgo.format(formatter);
        String endDate = now.format(formatter);

        getAndSaveAllStockDailyData(startDate, endDate);

    }

    public void getAndSaveAllStockDailyData(String startDate, String endDate){
// 旧逻辑，用的是按code遍历，需要4000～5000次访问api

//        获取A股所有公司代码
//        TushareResponse.ResponseDate datas = client.getStockBasics().getData();
//
//        for (Object item : datas.getItems()) {
//            List data = (List) item;
//
//            String tsCode = data.get(0).toString();
//
//            getAndSaveStockDailyData(tsCode,startDate,endDate);
//        }

        // 新逻辑  2023年02月21日

        // 1. 获取交易日期列表
        TushareResponse.ResponseDate data = client.getTradeCal(startDate,endDate).getData();

        // 2. 逐日遍历保存
        for (Object item : data.getItems()) {
            List datas = (List) item;

            String tradeDate = datas.get(0).toString();

            getAndSaveStockDailyData(tradeDate);
        }

    }


    public void updateStockDailyData() {
        // 获取并保存最新数据的代码
        LocalDate now = LocalDate.now();
        LocalDate yesterday = now.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String tradeDate = yesterday.format(formatter);

        getAndSaveStockDailyData( tradeDate);

    }



//    行情数据初始化
    public void initStockDailyData() {
        if (!isDataInitialized) {
            logger.info("Stock daily data has already been initialized.");
            return;
        }

        // 初始化数据的代码
        getAndSaveAllStockDailyData();

        isDataInitialized = false;
        logger.info("Stock daily data initialization completed.");
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
