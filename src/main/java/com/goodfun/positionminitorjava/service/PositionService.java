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
        BigDecimal profitLoss =realPrice.subtract(entity.getBuyInPrice()).multiply(BigDecimal.valueOf(entity.getNumber()));

        positionProfitLoss.setProfitLoss(profitLoss);

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


//        字体控制
        String colorStyle = "color:";
        String fontWeightStyle = "font-weight:bold;";

        String pLStyle = "";

        //颜色
        int compareColor = positionProfitLoss.getProfitLoss().compareTo(BigDecimal.ZERO);

        if(compareColor > 0 ){
            pLStyle = colorStyle + "red;";
        }
        else if(compareColor < 0 ){
            pLStyle = colorStyle + "green;";
        }

        //粗体
        int compareBold = positionProfitLoss.getProfitLossRate().multiply(new BigDecimal(100)).abs().compareTo(BigDecimal.TEN);
        if(compareBold > 0){
            pLStyle += fontWeightStyle;
        }

        positionProfitLoss.setPositionTablePLStyle(pLStyle);

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
