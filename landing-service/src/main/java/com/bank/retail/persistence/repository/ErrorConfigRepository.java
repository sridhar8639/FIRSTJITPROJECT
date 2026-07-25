package com.bank.retail.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.retail.persistence.entity.ErrorConfig;

public interface ErrorConfigRepository extends JpaRepository<ErrorConfig, Integer> {

	public Optional<ErrorConfig> findByUnitIdAndLangAndChannelIdAndMwErrorCodeAndStatus(String unit, String lang,
			String channel, String mwErrorCode, String status);

	public Optional<ErrorConfig> findByUnitIdAndLangAndChannelIdAndMwErrorCodeAndServiceTypeAndStatus(String unit,
			String lang, String channel, String mwErrorCode, String serviceId, String status);
	/**
     * Find error configuration by unit, language, channel, MW error code, service type and status
     * 
     * @param unitId Unit identifier
     * @param lang Language code
     * @param channelId Channel identifier
     * @param mwErrorCode Middleware error code
     * @param serviceType Service type
     * @param status Status to filter by
     * @return List of ErrorConfig entities matching the criteria
     */
    @Query("SELECT e FROM ErrorConfig e WHERE " +
           "e.unitId = :unitId AND " +
           "e.lang = :lang AND " +
           "e.channelId = :channelId AND " +
           "e.mwErrorCode = :mwErrorCode AND " +
           "e.serviceType = :serviceType AND " +
           "e.status = :status")
    List<ErrorConfig> findErrorConfigByCriteria(
            @Param("unitId") String unitId,
            @Param("lang") String lang,
            @Param("channelId") String channelId,
            @Param("mwErrorCode") String mwErrorCode,
            @Param("serviceType") String serviceType,
            @Param("status") String status);

    /**
     * Find error configuration by unit, language, channel and MW error code
     * 
     * @param unitId Unit identifier
     * @param lang Language code
     * @param channelId Channel identifier
     * @param mwErrorCode Middleware error code
     * @return List of ErrorConfig entities matching the criteria
     */
    @Query("SELECT e FROM ErrorConfig e WHERE " +
           "e.unitId = :unitId AND " +
           "e.lang = :lang AND " +
           "e.channelId = :channelId AND " +
           "e.mwErrorCode = :mwErrorCode")
    List<ErrorConfig> findErrorConfigByUnitLangChannelAndMwError(
            @Param("unitId") String unitId,
            @Param("lang") String lang,
            @Param("channelId") String channelId,
            @Param("mwErrorCode") String mwErrorCode);

    /**
     * Find error configuration by MW error code and status
     * 
     * @param mwErrorCode Middleware error code
     * @param status Status to filter by
     * @return List of ErrorConfig entities matching the criteria
     */
    List<ErrorConfig> findByMwErrorCodeAndStatus(String mwErrorCode, String status);

    /**
     * Find error configuration by OCS error code and status
     * 
     * @param ocsErrCode OCS error code
     * @param status Status to filter by
     * @return List of ErrorConfig entities matching the criteria
     */
    List<ErrorConfig> findByOcsErrCodeAndStatus(String ocsErrCode, String status);

    /**
     * Find all active error configurations
     * 
     * @param status Status to filter by (e.g., "ACTIVE")
     * @return List of ErrorConfig entities matching the criteria
     */
    List<ErrorConfig> findByStatus(String status);
}
