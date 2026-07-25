package com.bank.retail.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bank.retail.api.dto.ResultUtilVO;
import com.bank.retail.constants.AppConstant;
import com.bank.retail.persistence.entity.ErrorConfig;
import com.bank.retail.persistence.entity.ParamConfig;
import com.bank.retail.persistence.repository.ErrorConfigRepository;
import com.bank.retail.persistence.repository.ParamConfigRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for ErrorConfig operations
 * Provides methods to retrieve error codes and messages
 */
@Component
@Slf4j
@AllArgsConstructor
public class ErrorConfigUtil {

    private final ErrorConfigRepository errorConfigRepository;
    
    private final ParamConfigRepository paramConfigRepository;


    /**
     * Get error configuration and return ResultUtilVO object
     * 
     * @param unit Unit identifier
     * @param lang Language code
     * @param channel Channel identifier
     * @param mwErrorCode Middleware error code
     * @param serviceId Service identifier
     * @param status Status to filter by
     * @return ResultUtilVO object with error code and description
     */
    public ResultUtilVO getErrorConfig(String unit, String lang, String channel, 
                                     String mwErrorCode, String serviceId, String status) {
        try {
            log.debug("Retrieving error config for unit: {}, lang: {}, channel: {}, mwErrorCode: {}, serviceId: {}, status: {}", 
                     unit, lang, channel, mwErrorCode, serviceId, status);

            // First try to find exact match with all criteria
            List<ErrorConfig> errorConfigs = errorConfigRepository.findErrorConfigByCriteria(
                    unit, lang, channel, mwErrorCode, serviceId, status);

            if (!errorConfigs.isEmpty()) {
                ErrorConfig errorConfig = errorConfigs.get(0);
                log.debug("Found exact match error config - ocsErrCode: {}, ocsErrDesc: {}", 
                         errorConfig.getOcsErrCode(), errorConfig.getOcsErrDesc());
                
                return ResultUtilVO.error(errorConfig.getOcsErrCode(), errorConfig.getOcsErrDesc());
            }

            // If no configuration found, try to get default error from ParamConfig
            log.warn("No error configuration found for mwErrorCode: {}, trying ParamConfig for default error", mwErrorCode);
            return getDefaultErrorFromParamConfig(unit, channel, status,lang);

        } catch (Exception e) {
            log.error("Error retrieving error configuration: {}", e.getMessage(), e);
            return getDefaultErrorFromParamConfig(unit, channel, status,lang);
        }
    }

    /**
     * Get default error configuration from ParamConfig
     * 
     * @param unit Unit identifier
     * @param channel Channel identifier
     * @param status Status to filter by
     * @param lang 
     * @return ResultUtilVO with error code and description from ParamConfig or constants
     */
    private ResultUtilVO getDefaultErrorFromParamConfig(String unit, String channel, String status, String lang) {
        try {
            log.debug("Retrieving default error config from ParamConfig for unit: {}, channel: {}, status: {}", 
                     unit, channel, status);

            // Try to find GENERRORCODE configuration in ParamConfig
            List<ParamConfig> paramConfigs = paramConfigRepository.findConfigByKeyUnitChannelAndStatus(
                    AppConstant.DEFAULT_ERROR_CODE+"_"+lang.toUpperCase(), unit, channel, status);

            if (!paramConfigs.isEmpty()) {
                ParamConfig paramConfig = paramConfigs.get(0);
                log.debug("Found default error config in ParamConfig - confValue: {}, remarks: {}",
                         paramConfig.getConfValue(), paramConfig.getRemarks());

                return ResultUtilVO.error(AppConstant.DEFAULT_ERROR_CODE, paramConfig.getRemarks());
            }

            // If no ParamConfig found, return constants
            log.warn("No default error configuration found in ParamConfig, using constants");
            return ResultUtilVO.error(AppConstant.DEFAULT_ERROR_CODE, AppConstant.GEN_ERROR_DESC);

        } catch (Exception e) {
            log.error("Error retrieving default error configuration from ParamConfig: {}", e.getMessage(), e);
            return ResultUtilVO.error(AppConstant.DEFAULT_ERROR_CODE, AppConstant.GEN_ERROR_DESC);
        }
    }
}
