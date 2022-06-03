create table position
(
    id           int auto_increment
        primary key,
    code         varchar(10) null comment '股票代码',
    name         varchar(10) null comment '股票名称',
    buy_in_date  date        null comment '买入日期',
    buy_in_price float       null comment '买入价',
    status       tinyint     null comment '持仓1/平仓0',
    number       int         null comment '买入数量'
)
    comment '持仓情况';

INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (1, 'sh518880', '黄金ETF', '2022-05-11', 3.875, 1, 1000);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (2, 'sz000786', '北新建材', '2022-05-11', 30.33, 1, 100);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (3, 'sz002233', '塔牌集团', '2022-05-12', 8.83, 1, 200);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (4, 'sh600276', '恒瑞医药', '2022-05-12', 30.15, 1, 100);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (5, 'sh513300', '纳斯达克ETF', '2022-05-12', 1.025, 1, 1000);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (6, 'sh513500 ', '标普500ETF', '2022-05-12', 1.256, 1, 1000);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (7, 'sz000725 ', '京东方Ａ', '2022-05-13', 3.82, 1, 500);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (8, 'sh518880 ', '黄金ETF', '2022-05-16', 3.864, 1, 1000);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (9, 'sz002507 ', '涪陵榨菜', '2022-05-16', 34.36, 1, 100);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (10, 'sh600031', '三一重工', '2022-05-24', 16.45, 1, 200);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (11, 'sh518880 ', '黄金ETF', '2022-05-30', 3.887, 1, 500);
INSERT INTO stock_position.position (id, code, name, buy_in_date, buy_in_price, status, number) VALUES (12, 'sh600585 ', '海螺水泥', '2022-06-02', 36.12, 1, 100);