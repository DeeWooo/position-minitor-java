package com.goodfun.positionminitorjava.task;

import com.goodfun.positionminitorjava.service.HistoryQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TushareTask {



    @Autowired
    private HistoryQuoteService historyQuoteService;


    @Scheduled(cron = "0 0 1 * * ?") // 每天1点执行任务
    public void updateStockDailyData() {

//        数据初始化
        historyQuoteService.initStockDailyData();

        // 调用更新数据的方法
        historyQuoteService.updateStockDailyData();
    }
}
