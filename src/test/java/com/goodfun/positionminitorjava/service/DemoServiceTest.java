package com.goodfun.positionminitorjava.service;

import com.goodfun.positionminitorjava.PositionMinitorJavaApplicationTests;
import com.goodfun.positionminitorjava.dao.entity.Demo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DemoServiceTest extends PositionMinitorJavaApplicationTests {

    @Autowired
    DemoService demoService;

    @Test
    void show() {
        List<Demo> demos = demoService.show();
        System.out.println(demos);
    }
}