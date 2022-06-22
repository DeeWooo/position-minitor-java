package com.goodfun.positionminitorjava.controller;

import com.goodfun.positionminitorjava.dao.entity.Demo;
import com.goodfun.positionminitorjava.dao.entity.PositionEntity;
import com.goodfun.positionminitorjava.global.Portfolio;
import com.goodfun.positionminitorjava.model.PortfolioProfitLoss;
import com.goodfun.positionminitorjava.model.PositionProfitLoss;
import com.goodfun.positionminitorjava.service.PortfolioService;
import com.goodfun.positionminitorjava.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/index")
    public String index() {
        return "index";

    }

    @GetMapping("/portfolio")
    public String portfolio(Model model){

        List<PortfolioProfitLoss> portfolioProfitLosses = portfolioService.show();

        portfolioShow(model, portfolioProfitLosses);

        return "portfolio";

    }

    @GetMapping("/portfolio-refresh")
    public String portfolioRefresh(Model model){

        List<PortfolioProfitLoss> portfolioProfitLosses = portfolioService.show();

        portfolioShow(model, portfolioProfitLosses);

        return "portfolio::t2";

    }

    @GetMapping("/real-position")
    public String realPositon(Model model) {

        List<PositionProfitLoss> positionProfitLosses = positionService.show();


        positionShow(model, positionProfitLosses);

        return "real-position";
    }

    @GetMapping("/real-position-refresh")
    public String realPositonRf(Model model) {
        List<PositionProfitLoss> positionProfitLosses = positionService.show();

        positionShow(model, positionProfitLosses);
        return "real-position::t1";
    }

    @GetMapping("/real-position-single")
    public String realPositionOne(Model model, @RequestParam(name = "code") String code){
        System.out.println("单独持仓查询");
        List<PositionProfitLoss> positionProfitLosses = positionService.showOne(code);

        positionShow(model, positionProfitLosses);

        return "real-position::t1";
    }

    /**
     * 持仓页面展示
     *
     * @param model
     * @return
     */
    private void positionShow(Model model, List<PositionProfitLoss> positionProfitLosses ) {


        List<String> stocks = positionProfitLosses.stream()
                .map(PositionProfitLoss::getPositionName)
                .distinct()
                .collect(Collectors.toList());


        model.addAttribute("stocks", stocks);


        positionProfitLosses = positionProfitLosses.stream()
                .sorted(Comparator.comparing(PositionProfitLoss::getProfitLossRate,Comparator.reverseOrder()).thenComparing(PositionProfitLoss::getBuyInDateShow, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        //Comparator.comparing(test::getState).thenComparing(test::getTime,Comparator.reverseOrder())

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

    private void portfolioShow(Model model, List<PortfolioProfitLoss> positionProfitLosses){
        model.addAttribute("positons", positionProfitLosses);

    }

    private List<PortfolioProfitLoss> convertPositionProfitLoss2PortfolioProfitLossList(List<PositionProfitLoss> positionProfitLosses ) {
        List<Portfolio> portfolios = positionProfitLosses.stream()
                .map(PositionProfitLoss::getPosition)
                .map(PositionEntity::getPortfolio)
                .distinct().sorted()
                .collect(Collectors.toList());

        List<PortfolioProfitLoss> portfolioProfitLosses = portfolios.stream()
                .map(item->{
                    PortfolioProfitLoss portfolioProfitLoss = new PortfolioProfitLoss();
                    portfolioProfitLoss.setPortfolio(item);

                    return portfolioProfitLoss;
                }).collect(Collectors.toList());
        return portfolioProfitLosses;
    }

}
