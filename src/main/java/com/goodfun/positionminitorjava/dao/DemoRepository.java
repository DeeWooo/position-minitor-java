package com.goodfun.positionminitorjava.dao;

import com.goodfun.positionminitorjava.dao.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemoRepository extends JpaRepository<Demo,Long> {
    @Override
    List<Demo> findAll();
}
