package com.goodfun.positionminitorjava.config;

import com.goodfun.positionminitorjava.PositionMinitorJavaApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class RestTemplateConfigTest extends PositionMinitorJavaApplicationTests {
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void visitTest(){
//        String url = "http://hq.sinajs.cn/list={list}";
//        String url = "http://hq.sinajs.cn/list=sh601006";
        String url = "http://qt.gtimg.cn/q={code}";


//        list=sh601006
        Map<String,String> map =new HashMap<>();
        map.put("code","sh601006");

        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer","https://finance.sina.com.cn/");

//        List<Charset> acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
//        headers.setAcceptCharset(acceptCharset);


        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String result = restTemplate.getForObject(url, String.class, map);
//        String result = restTemplate.getForObject(url, String.class);
//        ResponseEntity<String> exchage = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);

        Pattern p1=Pattern.compile("\"(.*?)\"");
        Matcher m = p1.matcher(result);

        String values = "";
        while (m.find()) {

            values = m.group().toString().replace("\"","");
        }

        String[] list =  values.split("~");

        System.out.println(list[3]);

        System.out.println(result);
    }

}