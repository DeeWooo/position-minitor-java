
CREATE TABLE `stock_daily_data` (
                                    `stock_code` varchar(16) NOT NULL,
                                    `trade_date` datetime NOT NULL,
                                    `open_price` decimal(10,2) NOT NULL,
                                    `close_price` decimal(10,2) NOT NULL,
                                    `high_price` decimal(10,2) NOT NULL,
                                    `low_price` decimal(10,2) NOT NULL,
                                    `volume` decimal(20,0) NOT NULL,
                                    PRIMARY KEY (`stock_code`,`trade_date`),
                                    KEY `stock_code_index` (`stock_code`),
                                    KEY `trade_date_index` (`trade_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
