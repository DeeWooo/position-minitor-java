package com.goodfun.positionminitorjava.service;

import com.goodfun.positionminitorjava.dao.PositionRepository;
import com.goodfun.positionminitorjava.dao.entity.PositionEntity;
import com.goodfun.positionminitorjava.global.Portfolio;
import com.goodfun.positionminitorjava.global.PositionStatus;
import com.goodfun.positionminitorjava.global.Quote;
import com.goodfun.positionminitorjava.model.PortfolioProfitLoss;
import com.goodfun.positionminitorjava.model.PositionProfitLoss;
import com.goodfun.positionminitorjava.model.RealQuote;
import com.goodfun.positionminitorjava.model.TargetProfitLoss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionService positionService;


    public List<PortfolioProfitLoss> show(){

        List<PositionEntity> positionEntities = positionRepository.findAll();

        //过滤平仓数据
        positionEntities = positionEntities.stream()
                .filter(entity -> PositionStatus.CLOSE != entity.getStatus())
                .collect(Collectors.toList());


        //按投资组合分组
        Map<Portfolio,List<PositionEntity>> portfolioMap = positionEntities.stream().collect(Collectors.groupingBy(PositionEntity::getPortfolio));
//        Set<Map.Entry<Portfolio,List<PositionEntity>>> portfolioEntries = portfolioMap.entrySet();
        Set<Portfolio> portfolioKeySet = portfolioMap.keySet();
//        Collection<List<PositionEntity>> portfolioValues = portfolioMap.values();

        List<PortfolioProfitLoss> portfolioProfitLosses = portfolioKeySet.stream()
                        .map(item ->{
                            PortfolioProfitLoss portfolioProfitLoss = new PortfolioProfitLoss();
                            portfolioProfitLoss.setPortfolio(item);

                            List<PositionEntity> entities = portfolioMap.get(item);

                            //再按代码分组

                            Map<String,List<PositionEntity>> codeMap = entities.stream().collect(Collectors.groupingBy(PositionEntity::getCode));
                            Set<String> codeKeySet = codeMap.keySet();
                            List<TargetProfitLoss> targetProfitLossList = codeKeySet.stream()
                                    .map(codeItem ->{
//                                        if(codeItem.equals("sh600585")){
//                                            System.out.println(codeItem);
//                                        }
                                        TargetProfitLoss targetProfitLoss = new TargetProfitLoss();
                                        targetProfitLoss.setCode(codeItem);

                                        RealQuote realQuote = Quote.realQuoteMap.get(codeItem);
                                        targetProfitLoss.setName(realQuote.getName());
                                        targetProfitLoss.setRealPrice(realQuote.getRealPrice());



                                        List<PositionEntity> positionEntityList = codeMap.get(codeItem);

                                        List<PositionProfitLoss> positionProfitLosses = positionEntityList.stream()
                                                .map(e ->positionService.convertEntity2ProfitLoss(e))
                                                .sorted(Comparator.comparing(PositionProfitLoss::getBuyInDateShow).reversed())
                                                        .collect(Collectors.toList());

                                        targetProfitLoss.setPositionProfitLosses(positionProfitLosses);

                                        return targetProfitLoss;
                                    })
                                    .collect(Collectors.toList());

                            portfolioProfitLoss.setTargetProfitLossList(targetProfitLossList);

                            return  portfolioProfitLoss;
                        })
                .sorted(Comparator.comparing(PortfolioProfitLoss::getPortfolio))
                .collect(Collectors.toList());


        return portfolioProfitLosses;

    }

    private TargetProfitLoss processTargetProfitLoss(String code, Portfolio portfolio){
        TargetProfitLoss targetProfitLoss = new TargetProfitLoss();
//        targetProfitLoss.setCode(target);


        return targetProfitLoss;
    }


}
