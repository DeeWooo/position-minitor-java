package com.goodfun.positionminitorjava.util;

import com.goodfun.positionminitorjava.model.PositionProfitLoss;

import java.math.BigDecimal;

public class CommonUtils {

    public static String styleProcess(BigDecimal profitLoss,BigDecimal profitLossRate){


//        字体控制
        String colorStyle = "color:";
        String fontWeightStyle = "font-weight:bold;";

        String pLStyle = "";

        //颜色
        int compareColor = profitLoss.compareTo(BigDecimal.ZERO);

        if(compareColor > 0 ){
            pLStyle = colorStyle + "red;";
        }
        else if(compareColor < 0 ){
            pLStyle = colorStyle + "green;";
        }

        //粗体
        int compareBold = profitLossRate.multiply(new BigDecimal(100)).abs().compareTo(BigDecimal.TEN);
        if(compareBold > 0){
            pLStyle += fontWeightStyle;
        }

        return pLStyle;
    }
}
