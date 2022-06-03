package com.goodfun.positionminitorjava.controller;

import com.goodfun.positionminitorjava.dao.entity.Demo;
import com.goodfun.positionminitorjava.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HelloPage {

    @Autowired
    private DemoService demoService;


    @GetMapping("/hello")
    public String helloPage(Model model){
        List<Demo> demos = demoService.show();

        model.addAttribute("demos",demos);

        return "hello";
    }

    @GetMapping("/hello-refresh")
    public String helloRf(Model model){
        List<Demo> demos = demoService.show();

        model.addAttribute("demos",demos);

        return "hello::t1";
    }

}
