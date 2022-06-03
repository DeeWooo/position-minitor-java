# position-minitor-java
### 自用的实时持仓监控面板V0.1

#### 起因

原本用google表格做自己的持仓监控工具，实时行情用定时触发器来跑脚本， 但google表格的定时触发器时灵时不灵，盘中监控非常不及时。
不得不自己做一个小工具，来改善看盘体验。

#### 技术选型
springboot+thymeleaf+mysql


#### 基本设计：
1. 前端单纯展示加工后的数据
2. 前后端通过websocket进行实时数据交互
3. mysql维护每一笔买入持仓信息
4. 通过第三方接口获取实时价格
5. 通过定时任务将实时价格放入全局map

![image](https://user-images.githubusercontent.com/7700516/171778822-42d679a3-8eea-4505-bb8a-c424a237eacc.png)


#### 效果
V0.1

![image](https://user-images.githubusercontent.com/7700516/171778899-04f5fa8c-db7b-45a1-806a-fa8b54ab28f8.png)

