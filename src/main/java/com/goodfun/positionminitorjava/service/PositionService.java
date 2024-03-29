package com.goodfun.positionminitorjava.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

import com.goodfun.positionminitorjava.dao.PositionRepository;
import com.goodfun.positionminitorjava.dao.entity.PositionEntity;
import com.goodfun.positionminitorjava.global.PositionStatus;
import com.goodfun.positionminitorjava.global.Quote;
import com.goodfun.positionminitorjava.model.PositionProfitLoss;
import com.goodfun.positionminitorjava.util.CommonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private static final Logger logger = LoggerFactory.getLogger(PositionService.class);  


    @Autowired
    private PositionRepository positionRepository;


    public void saveRecord(PositionEntity positionEntity){
        logger.debug("PositionService.saveRecord.positionEntity-----------" + JSONUtil.toJsonStr(positionEntity));

        positionRepository.save(positionEntity);
    }


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

    public List<PositionProfitLoss> showOne(String code){

        List<PositionEntity> positionEntities = positionRepository.findAllByCode(code);


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
    public PositionProfitLoss convertEntity2ProfitLoss(PositionEntity entity){

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

        if (realPrice != null && entity.getBuyInPrice() != null) {
            BigDecimal profitLoss = realPrice.subtract(entity.getBuyInPrice()).multiply(BigDecimal.valueOf(entity.getNumber()));
            positionProfitLoss.setProfitLoss(profitLoss);
        }
       

        BigDecimal  profitLossRate = BigDecimal.ZERO;

        if (entity.getBuyInPrice().compareTo(BigDecimal.ZERO) != 0) {
//            System.out.println(entity.getCode());
            profitLossRate = realPrice.subtract(entity.getBuyInPrice()).divide(entity.getBuyInPrice(), 6, BigDecimal.ROUND_HALF_UP);
        }

//        DecimalFormat decimalFormat= new  DecimalFormat( "0.000000" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//        positionProfitLoss.setProfitLossRateShow(decimalFormat.format(profitLossRate)); //format 返回的是字符串

        positionProfitLoss.setProfitLossRate(profitLossRate);
        positionProfitLoss.setProfitLossRateShow(profitLossRate.toString());

        positionProfitLoss.setPositionCost(entity.getBuyInPrice().multiply(BigDecimal.valueOf(entity.getNumber())));

        positionProfitLoss.setPositionName(entity.getName());


//        投资组合
        positionProfitLoss.setPortfolioShow(entity.getPortfolio().getInfo());



        //前端样式控制
        styleProcess(positionProfitLoss);






        return positionProfitLoss;
    }

    /**
     * 实时持仓表页面字体控制
     * @param positionProfitLoss
     * @return
     */
    private PositionProfitLoss styleProcess(PositionProfitLoss positionProfitLoss){


        positionProfitLoss.setPositionTablePLStyle(CommonUtils.styleProcess(positionProfitLoss.getProfitLoss(), positionProfitLoss.getProfitLossRate()));

        return positionProfitLoss;
    }


    /**
     * 平仓
     * @param id
     */
    public void closePosition(Long id){
        PositionEntity entity = positionRepository.findTopById(id);
        entity.setStatus(PositionStatus.CLOSE);
        positionRepository.save(entity);
    }
}
