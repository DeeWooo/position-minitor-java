package com.goodfun.positionminitorjava.controller;

import com.goodfun.positionminitorjava.dao.entity.Demo;
import com.goodfun.positionminitorjava.model.PositionProfitLoss;
import com.goodfun.positionminitorjava.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Router {

    @Autowired
    private PositionService positionService;

    @GetMapping("/real-position")
    public String realPositon(Model model) {

        positionShow(model);

        return "real-position";
    }

    @GetMapping("/real-position-refresh")
    public String realPositonRf(Model model) {

        positionShow(model);
        return "real-position::t1";
    }


    /**
     * 持仓页面展示
     *
     * @param model
     * @return
     */
    private void positionShow(Model model) {

        List<PositionProfitLoss> positionProfitLosses = positionService.show();

        positionProfitLosses = positionProfitLosses.stream()
                .sorted(Comparator.comparing(PositionProfitLoss::getBuyInDateShow).reversed())
                .collect(Collectors.toList());


        model.addAttribute("positons", positionProfitLosses);

        BigDecimal sumProfitLosses = positionProfitLosses.stream()
                .map(PositionProfitLoss::getProfitLoss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("sumProfitLosses", sumProfitLosses);

        BigDecimal totalPositionCost = positionProfitLosses.stream()
                .map(PositionProfitLoss::getPositionCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalPositionCost", totalPositionCost);

        String totalProfitLosseRate = sumProfitLosses.divide(totalPositionCost,6,BigDecimal.ROUND_HALF_UP).toString();

        model.addAttribute("totalProfitLosseRate", totalProfitLosseRate);


    }


}
