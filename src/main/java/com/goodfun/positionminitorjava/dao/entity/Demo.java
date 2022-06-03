package com.goodfun.positionminitorjava.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "demo")
public class Demo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private Float name;
}
