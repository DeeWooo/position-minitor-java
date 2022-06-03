package com.goodfun.positionminitorjava.controller;

import com.goodfun.positionminitorjava.dao.entity.Demo;
import com.goodfun.positionminitorjava.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Hello {

    @Autowired
    private DemoService demoService;


    @GetMapping("/")
    public  String Hello(){
        return "hello";
    }

    @PostMapping("/update-name")
    public Demo updateName(@RequestParam(name = "i11") Float name){
        Demo demo = new Demo();
        demo.setId(1L);
        demo.setName(name);

        demoService.save(demo);

        return demo;
    }

    @GetMapping("/refresh-price")
    public void refreshPrice(){
        demoService.refreshPrice();
    }



}
