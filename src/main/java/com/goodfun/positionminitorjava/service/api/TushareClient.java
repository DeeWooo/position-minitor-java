package com.goodfun.positionminitorjava.service.api;

import com.goodfun.positionminitorjava.model.StockDailyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class TushareClient {

    @Value("${pm.param.tushareToken}")
    private String tushareToken;
//    private String tushareToken = "9897de9903a7ce04fa4b85bab5e531b6033d63e197faa87b7de5317f";
    private String url = "https://api.tushare.pro";


    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;


    @Autowired
    public TushareClient(RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    public TushareResponse getStockDailyData(String tsCode, String startDate, String endDate) {

        // 构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "daily");
        params.put("token", tushareToken);
        params.put("ts_code", tsCode);
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("fields", "ts_code,trade_date,open,high,low,close,vol");

        return  tushareRequest(params);
    }

    public TushareResponse getStockDailyData(String tradeDate) {

        // 构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "daily");
        params.put("token", tushareToken);
        params.put("trade_date", tradeDate);
        params.put("fields", "ts_code,trade_date,open,high,low,close,vol");

        return  tushareRequest(params);

    }


    public TushareResponse getTradeCal(String startDate, String endDate) {
        // 构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "trade_cal");
        params.put("token", tushareToken);
        params.put("exchange", "SSE");
        params.put("is_open", "1");
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("fields", "cal_date");

        return  tushareRequest(params);

    }

    public TushareResponse getStockBasics(){
        // 构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "stock_basic");
        params.put("token", tushareToken);
        params.put("fields", "ts_code,symbol,name,area,industry,list_date");

        Map<String, Object> subparams = new HashMap<>();
        subparams.put("list_status","L");

        params.put("params", subparams);


        return  tushareRequest(params);
    }

    private TushareResponse tushareRequest(Map<String, Object> params)  {
        // 定义请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 定义请求体
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);


//        由于用户积分不够，每分钟只能访问50次，所以每次请求前，延迟5s，确保每天请求数足够
        try {
            Thread.sleep(5000); // 5000毫秒，即5秒
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        // 包装RestTemplate，实现重试逻辑
        ResponseEntity<TushareResponse> responseEntity = retryTemplate.execute(context -> {
            return restTemplate.postForEntity(url, request, TushareResponse.class);
        });

        // 处理响应
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to fetch data from tushare API. Response: " + responseEntity.toString());
        }
    }

}

