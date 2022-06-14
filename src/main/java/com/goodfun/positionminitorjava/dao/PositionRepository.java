package com.goodfun.positionminitorjava.dao;

import com.goodfun.positionminitorjava.dao.entity.PositionEntity;
import com.goodfun.positionminitorjava.global.PositionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<PositionEntity,Long> {

    @Override
    List<PositionEntity> findAll();

    List<PositionEntity> findAllByCode(String code);

    List<PositionEntity> findAllByStatus(PositionStatus status);

    PositionEntity findTopById(Long id);

}
