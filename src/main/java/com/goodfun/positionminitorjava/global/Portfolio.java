package com.goodfun.positionminitorjava.global;

public enum Portfolio {
    subjective("主观持仓"), //主观持仓 0
    nonrmb("非RMB"), //非rmb 1
    rmbetf("RMBETF"), //2
    northbound("北上资金"), //北上资金 3
    dougua("豆瓜"), //豆瓜 4
    peg("peg策略"), //5
    whitehouse("白马成长策略"); //白马成长 6


    private String info;

    private Portfolio(String info){
        this.info = info;
    }

    public String getInfo(){
        return this.info;
    }

}
