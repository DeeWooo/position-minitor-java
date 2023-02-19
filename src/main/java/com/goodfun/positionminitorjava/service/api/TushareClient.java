package com.goodfun.positionminitorjava.service.api;

import com.goodfun.positionminitorjava.model.StockDailyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class TushareClient {

    private String tushareToken = "9897de9903a7ce04fa4b85bab5e531b6033d63e197faa87b7de5317f";


    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;


    @Autowired
    public TushareClient(RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    public TushareResponse<StockDailyData> getStockDailyData(String tsCode, String startDate, String endDate) {
        String url = "https://api.tushare.pro";

        // 构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "daily");
        params.put("token", tushareToken);
        params.put("ts_code", tsCode);
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("fields", "ts_code,trade_date,open,high,low,close,vol");


        // 定义请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 定义请求体
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);


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

