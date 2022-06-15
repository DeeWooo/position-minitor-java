alter table position
    add portfolio int null comment '投资组合';

alter table position modify portfolio tinyint null comment '投资组合';
