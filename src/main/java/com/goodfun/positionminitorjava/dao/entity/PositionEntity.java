package com.goodfun.positionminitorjava.dao.entity;

import cn.hutool.core.date.DateTime;
import com.goodfun.positionminitorjava.global.PositionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "position")
public class PositionEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;


    @Column(name = "buy_in_date")
    private Date buyInDate;


    @Column(name = "buy_in_price")
    private BigDecimal buyInPrice;

    @Column(name = "status")
    private PositionStatus status;


    @Column(name = "number")
    private Integer number;
}
