package com.goodfun.positionminitorjava.controller;


import com.goodfun.positionminitorjava.dao.entity.PositionEntity;

import com.goodfun.positionminitorjava.global.PositionStatus;
import com.goodfun.positionminitorjava.service.PositionService;
import com.goodfun.positionminitorjava.service.api.RealQuoteService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import javax.validation.Valid;


@RestController
@Validated // 开启控制器方法参数校验
public class RealQuoteController {
    private static final Logger logger = LoggerFactory.getLogger(RealQuoteController.class);  

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

        System.out.println("装填行情map完毕！");
    }


    @GetMapping("/close-position")
    public void closePosition(@RequestParam(name = "tid") Long tid){
        System.out.println("平仓");
        positionService.closePosition(tid);

    }


    @PostMapping("/save-record")
    public String saveRecord( @RequestBody @Valid PositionEntity positionEntity){
//        logger.info("positionEntity-----------" + JSONUtil.toJsonStr(positionEntity));
        positionEntity.setBuyInDate(DateUtil.date());
        positionEntity.setStatus(PositionStatus.POSITION);

         positionService.saveRecord(positionEntity);
        return "SUCESS!";

    }

    // 投资组合列表



    

}
