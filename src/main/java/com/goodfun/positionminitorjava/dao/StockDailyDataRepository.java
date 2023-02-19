package com.goodfun.positionminitorjava.dao;


import com.goodfun.positionminitorjava.dao.entity.StockDailyDataEntity;
import com.goodfun.positionminitorjava.dao.entity.StockDailyDataId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockDailyDataRepository extends JpaRepository<StockDailyDataEntity, StockDailyDataId> {

}
