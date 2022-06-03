package com.goodfun.positionminitorjava.config;

import com.goodfun.positionminitorjava.controller.RealQuoteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.Access;

/**
 * 定时任务
 */
@Component
public class Jobs {

    @Autowired
    private RealQuoteController realQuoteController;

    @Scheduled(cron = "0/20 * * * * *")
    public void cronJob4QuoteMap(){
        System.out.println("20s定时任务");
        realQuoteController.refreshQuote();
    }
}
