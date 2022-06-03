package com.goodfun.positionminitorjava.service;

import com.goodfun.positionminitorjava.dao.DemoRepository;
import com.goodfun.positionminitorjava.dao.entity.Demo;
import com.goodfun.positionminitorjava.service.api.RealQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {

    @Autowired
    private RealQuoteService realQuoteService;

    @Autowired
    private DemoRepository demoRepository;

    public List<Demo> show(){
        return demoRepository.findAll();
    }

    public void save(Demo demo){
        demoRepository.save(demo);
    }


    public void refreshPrice(){
        String code = "sh601006";
        Float price = realQuoteService.getStockRealPrice(code);
        Demo demo = new Demo();
        demo.setId(1L);
        demo.setName(price);
        save(demo);
    }
}
