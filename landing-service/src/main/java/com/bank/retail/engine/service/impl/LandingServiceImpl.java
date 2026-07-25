package com.bank.retail.engine.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.retail.api.dto.AppExceptionHandlerUtilDto;
import com.bank.retail.api.dto.GenericResponse;
import com.bank.retail.api.dto.ResultUtilVO;
import com.bank.retail.constants.AppConstant;
import com.bank.retail.constants.HeaderConstants;
import com.bank.retail.constants.LandingConstants;
import com.bank.retail.engine.service.LandingService;
import com.bank.retail.persistence.repository.CurrencyRepository;
import com.bank.retail.persistence.repository.ErrorConfigRepository;
import com.bank.retail.persistence.repository.MockResponseDataRepositoryy;
import com.bank.retail.util.CommonUtil;
import com.bank.retail.util.ErrorConfigUtil;
import com.bank.retail.util.MockDataUtil;
import com.bank.retail.util.ResponseExtractionUtil;
import com.digi.oc.adapter.service.AdapterFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class LandingServiceImpl implements LandingService,LandingConstants {

	
	private final MockDataUtil mockDataUtil;

	@Autowired
	private ErrorConfigUtil errorConfigUtil;

	@Autowired
	private AdapterFlowService adapterFlowService;

	@Autowired
	private MockResponseDataRepositoryy mockResponseDataRepositary;

	@Autowired
	private ErrorConfigRepository errorConfigRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public GenericResponse<Map<String, Object>> getAccountDetailsService(Map<String, String> headers,Map<String, Object> requestBody)
	{

		String unitId = headers.get(HeaderConstants.UNIT_HEADER);
		String channelId = headers.get(HeaderConstants.CHANNEL_HEADER);
		String langCode = headers.get(HeaderConstants.ACCEPT_LANGUAGE_HEADER);
		String mwServiceId = headers.get(HeaderConstants.SERVICE_ID_HEADER);

		ResultUtilVO resultutilVo = new ResultUtilVO(AppConstant.RESULT_CODE, AppConstant.RESULT_DESC);
		GenericResponse<Map<String, Object>> finalResponse = new GenericResponse<>();
		Map<String, Object> dataMap = new LinkedHashMap<>();
		Map<String, Object> mockResponse = new LinkedHashMap();

		try {
			var appExeDto = new AppExceptionHandlerUtilDto(unitId, channelId, langCode, mwServiceId);
			
			String configValue = mockResponseDataRepositary.findConfigValueByKeyAndChannel(AppConstant.CONFIG_KEY_MW_SERVICE_ENV, channelId, AppConstant.STATUS);

			if (AppConstant.CONFIG_VALUE.equalsIgnoreCase(configValue)) 
			{
				log.info("Using Mock Data");
				mockResponse = mockDataUtil.execute(LANDING_SERVICE_NAME, mwServiceId);
			}
			else 
			{
				log.info("Using Adapter Flow Service");
				mockResponse = adapterFlowService.execute(requestBody, mwServiceId,
						headers.get(HeaderConstants.MODULE_ID),channelId,unitId);
				log.debug("Adapter response received, keys: {}", mockResponse != null ? mockResponse.keySet() : "null");
			}
			 log.debug("mockResponse Response is null: {}", mockResponse == null);
	         log.info("mockResponse Response is empty: {}", mockResponse != null && mockResponse.isEmpty());
			if (mockResponse == null || mockResponse.isEmpty())
			{
				log.warn("mockResponse Response is null or empty for serviceId={}", mwServiceId);
				return defaultError(unitId, langCode, channelId, mwServiceId);
			}

			// Extract response
			Map<String, Object> accDetailsRes = ResponseExtractionUtil.findNestedMapByKey(mockResponse, GET_ACCOUNT_DETAILS_RESPONSE);
			if (accDetailsRes == null || accDetailsRes.isEmpty())
			{
				log.error("GetAccountDetailsResponse not found in adapter response for serviceId={}", mwServiceId);
				return defaultError(unitId, langCode, channelId, mwServiceId);
			}
			log.debug("accDetailsRes Keys: {}", accDetailsRes.keySet());
			Map<String, Object> resultSet = ResponseExtractionUtil.findNestedMapByKey(accDetailsRes, RESULT_SET);
			if (resultSet == null ||resultSet.isEmpty()) 
			{
				log.error("{} not found in GetAccountDetailsResponse for serviceId={}", RESULT_SET, mwServiceId);
				return defaultError(unitId, langCode, channelId, mwServiceId);
			}
			log.info("ResultSet Keys: {}", resultSet.keySet());
			String status = ResponseExtractionUtil.extractValue(resultSet, STATUS);
			String message = ResponseExtractionUtil.extractValue(resultSet, MESSAGE);
			String processId = ResponseExtractionUtil.extractValue(resultSet, PROCESS_ID);
			String errorFlag = ResponseExtractionUtil.extractValue(resultSet, ERROR_FLAG);
			  	log.info("Final extracted values:");
	            log.info("  Status: '{}'", status);
	            log.info("  Message: '{}'", message);
	            log.info("  ProcessID: '{}'", processId);
	            log.info("  ErrorFlag: '{}'", errorFlag);

			if (STATUS_SUCCESS.equals(status))
			{
				dataMap.put("accountName", accDetailsRes.get(ACCOUNT_NAME));
				dataMap.put("accountNumber", accDetailsRes.get(ACCOUNT_NUMBER));
				dataMap.put("ledgerName", accDetailsRes.get(LEDGER_NAME));
				dataMap.put("currencyName", accDetailsRes.get(CURRENCY_NAME));
				String currname = ResponseExtractionUtil.extractValue(accDetailsRes, CURRENCY_NAME);
				var currency = currencyRepository.findByDescriptionAndUnitId(currname,unitId);
				var currencyDto = currency.isPresent() ? currency.get() : null;
				if(currencyDto!=null)
				{
				String currCode = currencyDto.getId().getCode();
				if(currCode!=null)
				{
					dataMap.put("currencyCode", currCode);
				}
				}
				dataMap.put("dateOpen", accDetailsRes.get(DATE_OPEN));
				dataMap.put("currentBal", accDetailsRes.get(CURRENT_BAL));
				dataMap.put("clearBal", accDetailsRes.get(CLEAR_BAL));
				dataMap.put("availableBal", accDetailsRes.get(AVAILABLE_BAL));
				dataMap.put("uuid", UUID.randomUUID().toString());
			    finalResponse.setData(dataMap);
			}
			else
			{
				resultutilVo = CommonUtil.getErrorDetailsByErrCode(appExeDto, errorConfigRepository, resultSet);
			}
		} catch (Exception e) {
			log.error("Unexpected error in landing account details", e);
			return defaultError(unitId, langCode, channelId, mwServiceId); 
		}
		finalResponse.setStatus(resultutilVo);
		return finalResponse;
	}

	private GenericResponse<Map<String, Object>> defaultError(String unitId, String langCode, String channelId, String mwServiceId) {
		return GenericResponse.error(errorConfigUtil.getErrorConfig(unitId, langCode, channelId,
				AppConstant.DEFAULT_ERROR_CODE, mwServiceId, AppConstant.STATUS));
	}

}
