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
import com.goodfun.positionminitorjava.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
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
                            portfolioProfitLoss.setFullPosition(Quote.FULL_POSITION);

                            List<PositionEntity> entities = portfolioMap.get(item);

                            //再按代码分组

                            Map<String,List<PositionEntity>> codeMap = entities.stream().collect(Collectors.groupingBy(PositionEntity::getCode));
                            Set<String> codeKeySet = codeMap.keySet();
                            List<TargetProfitLoss> targetProfitLossList = codeKeySet.stream()
                                    .map(codeItem ->{
                                        TargetProfitLoss targetProfitLoss = new TargetProfitLoss();
                                        targetProfitLoss.setCode(codeItem);

                                        //todo 处理realQuoteMap初始化还没有数据的时间间隔
                                        RealQuote realQuote = Quote.realQuoteMap.get(codeItem);
                                        targetProfitLoss.setName(realQuote.getName());
                                        targetProfitLoss.setRealPrice(realQuote.getRealPrice());



                                        List<PositionEntity> positionEntityList = codeMap.get(codeItem);

                                        List<PositionProfitLoss> positionProfitLosses = positionEntityList.stream()
                                                .map(e ->positionService.convertEntity2ProfitLoss(e))
                                                .sorted(Comparator.comparing(PositionProfitLoss::getBuyInDateShow).reversed())
                                                        .collect(Collectors.toList());

                                        targetProfitLoss.setPositionProfitLosses(positionProfitLosses);

                                        //计算成本仓位
                                        Double costPosition = positionEntityList.stream().reduce(0.0,(x,y)-> x+(y.getBuyInPrice().doubleValue() * y.getNumber()),Double::sum);

                                        targetProfitLoss.setCostPositionRate(new BigDecimal(costPosition).divide(Quote.FULL_POSITION,6, BigDecimal.ROUND_HALF_UP));

                                        //计算当前仓位
                                        Integer currentNumber = positionEntityList.stream().mapToInt(PositionEntity::getNumber).sum();

                                        BigDecimal currentPosition = realQuote.getRealPrice().multiply(new BigDecimal(currentNumber));
                                        targetProfitLoss.setCurrentPositionRate(currentPosition.divide(Quote.FULL_POSITION,6,
                                                BigDecimal.ROUND_HALF_UP));

                                        //计算盈亏
                                        BigDecimal profitLoss = positionProfitLosses.stream()
                                                .map(PositionProfitLoss::getProfitLoss)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        targetProfitLoss.setTargetProfitLoss(profitLoss);

//                                        System.out.println(codeItem+"-"+realQuote.getName()+"-"+costPosition);
                                        //计算盈亏比
                                        BigDecimal profitLossRate = (costPosition.equals(0.0)
                                        ?BigDecimal.ZERO:profitLoss.divide(new BigDecimal(costPosition),6, BigDecimal.ROUND_HALF_UP));
                                        targetProfitLoss.setTargetProfitLossRate(profitLossRate);

                                        targetProfitLoss.setPLStyle(CommonUtils.styleProcess(profitLoss, profitLossRate));

                                        //todo targetProfitLoss对象增加建议买入点、建议卖出点属性

                                        return targetProfitLoss;
                                    })
                                    .collect(Collectors.toList());

                            portfolioProfitLoss.setTargetProfitLossList(targetProfitLossList);

                            //计算盈亏
                            BigDecimal sumProfitLosses = targetProfitLossList.stream()
                                    .map(TargetProfitLoss::getTargetProfitLoss)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            portfolioProfitLoss.setSumProfitLosses(sumProfitLosses);

                            //总持仓成本
                            BigDecimal sumCostPositionRate = targetProfitLossList.stream()
                                    .map(TargetProfitLoss::getCostPositionRate)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            portfolioProfitLoss.setSumPositionCost(sumCostPositionRate.multiply( portfolioProfitLoss.getFullPosition()));

                            //总盈亏比
                            BigDecimal sumProfitLossesRate = sumProfitLosses.divide(sumCostPositionRate.multiply(portfolioProfitLoss.getFullPosition()),6,BigDecimal.ROUND_HALF_UP);
                            portfolioProfitLoss.setSumProfitLossesRate(sumProfitLossesRate);


                            //盈亏字体样式
                            String pLStyle = CommonUtils.styleProcess(sumProfitLosses,sumProfitLossesRate);
                            portfolioProfitLoss.setPLStyle(pLStyle);

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
