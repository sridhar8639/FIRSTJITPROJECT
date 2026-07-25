package com.bank.retail.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.retail.persistence.entity.MockResponseData;

@Repository
public interface MockResponseDataRepositoryy extends JpaRepository<MockResponseData, Long> {

    /**
     * Find mock response data by microservice and category code
     * @param microservice the microservice name
     * @param categoryCode the category code
     * @return Optional containing the mock response data if found
     */
    Optional<MockResponseData> findByMicroserviceAndCategoryCode(String microservice, String categoryCode);
    
    @Query(value = "SELECT CONF_VALUE FROM OCS_T_PARAM_CONFIG " +
            "WHERE CONF_KEY = :configKey " +
            "AND CHANNEL_ID = :channel " +
            "AND STATUS = :status " +
            "ORDER BY TXN_ID DESC LIMIT 1",
    nativeQuery = true)
     String findConfigValueByKeyAndChannel(@Param("configKey") String configKey, @Param("channel") String channel,
             @Param("status") String status);

    
}


