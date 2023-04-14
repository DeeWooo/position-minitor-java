package com.goodfun.positionminitorjava.service.api;

import com.goodfun.positionminitorjava.controller.RealQuoteController;
import com.goodfun.positionminitorjava.global.PositionStatus;
import com.goodfun.positionminitorjava.global.Quote;
import com.goodfun.positionminitorjava.model.RealQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * todo 1 访问一次接口，解析多条数据
 * todo 4 优化访问api，现在的不优雅
 */
@Service
public class RealQuoteService {
    private static final Logger logger = LoggerFactory.getLogger(RealQuoteService.class);



    private String stockUrl = "http://qt.gtimg.cn/q={code}";

    @Autowired
    RestTemplate restTemplate;

    public float getStockRealPrice(String code){
//        list=sh601006
        Map<String,String> map =new HashMap<>();
//        map.put("code","sh601006");
        map.put("code",code.trim());

        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer","https://finance.sina.com.cn/");

//        List<Charset> acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
//        headers.setAcceptCharset(acceptCharset);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String result = restTemplate.getForObject(stockUrl, String.class, map);
//        String result = restTemplate.getForObject(url, String.class);
//        ResponseEntity<String> exchage = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);


        Pattern p1=Pattern.compile("\"(.*?)\"");
        Matcher m = p1.matcher(result);

        String values = "";
        while (m.find()) {

            values = m.group().toString().replace("\"","");
        }

        String[] list =  values.split("~");
        return Float.valueOf(list[3]);
    }

    /**
     *  组装实时行情数据，放入本地缓存
     * @return
     */
    public void buildRealQuoteMap(String code){

        //放入map
        Quote.realQuoteMap.put(code,getRealQuote(code));
    }


    private RealQuote getRealQuote(String code){
//        logger.info("getRealQuote(String code)~~~~~"+code);
        //访问api
        String result = getOneStockRealQuoteFromAPI(code);

        //解析结果
        Pattern p1=Pattern.compile("\"(.*?)\"");
        Matcher m = p1.matcher(result);

        String values = "";
        while (m.find()) {

            values = m.group().toString().replace("\"","");
        }

        String[] list =  values.split("~");

        //组装结构
        //实时价格
        BigDecimal realPrice = new BigDecimal(Float.valueOf(list[3]));
        String name = list[1];

        // 组装RealQuote
        RealQuote realQuote = new RealQuote();
        realQuote.setCode(code);
        realQuote.setName(name);
        realQuote.setRealPrice(realPrice.setScale(4,BigDecimal.ROUND_HALF_UP));


        return realQuote;



    }

    private String  getOneStockRealQuoteFromAPI(String code){
        Map<String,String> map =new HashMap<>();
//        map.put("code","sh601006");
        map.put("code",code.trim());

        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer","https://finance.sina.com.cn/");

//        List<Charset> acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
//        headers.setAcceptCharset(acceptCharset);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        return restTemplate.getForObject(stockUrl, String.class, map);
    }


    public static void main(String[] args) {
         System.out.println(PositionStatus.POSITION.ordinal());
         System.out.println(PositionStatus.CLOSE.ordinal());
    }




}
