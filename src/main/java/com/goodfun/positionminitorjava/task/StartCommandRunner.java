package com.goodfun.positionminitorjava.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.goodfun.positionminitorjava.controller.RealQuoteController;

@Component
public class StartCommandRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartCommandRunner.class);  


    @Autowired
    private RealQuoteController realQuoteController;

    @Override
    public void run(String... args) throws Exception {
        //在应用程序启动时，调用refreshQuote()
        logger.info("等待5s");
        Thread.sleep(5000L);

        logger.info("开始装填行情map！");
        realQuoteController.refreshQuote();
    }
}