package com.goodfun.positionminitorjava.service;

import cn.hutool.core.date.DateUtil;
import com.goodfun.positionminitorjava.dao.PositionRepository;
import com.goodfun.positionminitorjava.dao.entity.PositionEntity;
import com.goodfun.positionminitorjava.global.PositionStatus;
import com.goodfun.positionminitorjava.global.Quote;
import com.goodfun.positionminitorjava.model.PositionProfitLoss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    /**
     * 持仓盈亏现状
     * @return
     */
    public List<PositionProfitLoss> show(){

        List<PositionEntity> positionEntities = positionRepository.findAll();

        return positionEntities.stream().filter(entity -> PositionStatus.CLOSE != entity.getStatus())
                .map(item-> convertEntity2ProfitLoss(item) )
                .collect(Collectors.toList());
    }

    /**
     * 获取去重后所有持仓中的代码
     * @return
     */
    public List<String> getCodesInPosition(){
        List<PositionEntity> entities = positionRepository.findAllByStatus(PositionStatus.POSITION);

        return entities.stream()
                .map(PositionEntity::getCode)
                .distinct().collect(Collectors.toList());

    }
    /**
     * 拼组实时盈亏
     * @param entity
     * @return
     */
    private PositionProfitLoss convertEntity2ProfitLoss(PositionEntity entity){

        PositionProfitLoss positionProfitLoss = new PositionProfitLoss();
        positionProfitLoss.setPosition(entity);

        //买入日期显示
        positionProfitLoss.setBuyInDateShow(DateUtil.format(entity.getBuyInDate(), "yyyy-MM-dd"));

        BigDecimal realPrice = BigDecimal.ZERO;
        if(Quote.realQuoteMap!=null){
            if(Quote.realQuoteMap.get(entity.getCode())!=null){
                realPrice = Quote.realQuoteMap.get(entity.getCode()).getRealPrice()==null?BigDecimal.ZERO:Quote.realQuoteMap.get(entity.getCode()).getRealPrice();
            }
        }
        positionProfitLoss.setRealPrice(realPrice);
        BigDecimal profitLoss =realPrice.subtract(entity.getBuyInPrice()).multiply(BigDecimal.valueOf(entity.getNumber()));

        positionProfitLoss.setProfitLoss(profitLoss);

        BigDecimal  profitLossRate = realPrice.subtract(entity.getBuyInPrice()).divide(entity.getBuyInPrice(),6, BigDecimal.ROUND_HALF_UP);
//        DecimalFormat decimalFormat= new  DecimalFormat( "0.000000" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//        positionProfitLoss.setProfitLossRateShow(decimalFormat.format(profitLossRate)); //format 返回的是字符串
        positionProfitLoss.setProfitLossRateShow(profitLossRate.toString());

        positionProfitLoss.setPositionCost(entity.getBuyInPrice().multiply(BigDecimal.valueOf(entity.getNumber())));

        return positionProfitLoss;
    }
}
