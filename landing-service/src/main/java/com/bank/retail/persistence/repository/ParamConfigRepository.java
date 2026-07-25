package com.bank.retail.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.retail.persistence.entity.ParamConfig;

@Repository
public interface ParamConfigRepository extends JpaRepository<ParamConfig, Long> {

    /**
     * Find configuration parameters by specific key, unit, channel and status
     *
     * @param confKey Configuration key
     * @param unitId Unit identifier
     * @param channelId Channel identifier
     * @param status Status to filter by
     * @return List of ParamConfig entities matching the criteria
     */
    @Query("SELECT p FROM ParamConfig p WHERE " +
           "p.confKey = :confKey AND " +
           "p.unitId = :unitId AND " +
           "p.channelId = :channelId AND " +
           "p.status = :status")
    List<ParamConfig> findConfigByKeyUnitChannelAndStatus(
            @Param("confKey") String confKey,
            @Param("unitId") String unitId,
            @Param("channelId") String channelId,
            @Param("status") String status);
}