package com.goodfun.positionminitorjava.controller;

import com.goodfun.positionminitorjava.service.PositionService;
import com.goodfun.positionminitorjava.service.api.RealQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RealQuoteController {

    @Autowired
    private RealQuoteService realQuoteService;

    @Autowired
    private PositionService positionService;

    @GetMapping("/refresh-quote")
    public void refreshQuoteAPI(){
        refreshQuote();
    }

    @GetMapping("/refresh-quote-single")
    public void refresQuoteSingle(@RequestParam(name = "code") String code){
        System.out.println(" 单独更新map");
        realQuoteService.buildRealQuoteMap(code);
    }



    public void refreshQuote(){
        //1. 拿code
        List<String> codes =positionService.getCodesInPosition();
        //2. 拿code对应的行情信息,并更新map
        codes.stream().forEach(code ->{
            realQuoteService.buildRealQuoteMap(code);
        });

        System.out.println("装填行情map");
    }

}
