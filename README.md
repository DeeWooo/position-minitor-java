# position-minitor-java
自用的实时持仓监控面板V0.1

技术选型：springboot+thymeleaf+mysql


基本设计：
1. 前端单纯展示加工后的数据
2. 前后端通过websocket进行实时数据交互
3. mysql维护每一笔买入持仓信息
4. 通过第三方接口获取实时价格
5. 通过定时任务将实时价格放入全局map

![image](https://user-images.githubusercontent.com/7700516/171778822-42d679a3-8eea-4505-bb8a-c424a237eacc.png)


V0.1效果

![image](https://user-images.githubusercontent.com/7700516/171778899-04f5fa8c-db7b-45a1-806a-fa8b54ab28f8.png)

