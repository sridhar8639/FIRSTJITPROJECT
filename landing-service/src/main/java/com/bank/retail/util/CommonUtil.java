package com.bank.retail.util;

import java.util.Map;
import java.util.Objects;
import com.bank.retail.api.dto.AppExceptionHandlerUtilDto;
import com.bank.retail.api.dto.ResultUtilVO;
import com.bank.retail.constants.AppConstant;
import com.bank.retail.persistence.repository.ErrorConfigRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

	public static ResultUtilVO getErrorDetailsByErrCode(AppExceptionHandlerUtilDto appDto,
			ErrorConfigRepository errorConfigRepository, Map<String, Object> resultSet) {
		String errCode = "";
		Object statusVal = Objects.nonNull(resultSet) ? resultSet.get("Status") : null;
		if (statusVal != null && !String.valueOf(statusVal).isBlank()) {
			errCode = String.valueOf(statusVal);
			log.info("Error code {} message {}", statusVal, resultSet.get("Message"));

			try {
				return errorConfigRepository
						.findByUnitIdAndLangAndChannelIdAndMwErrorCodeAndServiceTypeAndStatus(appDto.getUnit(),
								appDto.getLang(), appDto.getChannel(), errCode, appDto.getServiceId(),
								AppConstant.STATUS)
						.map(errorConfig -> {
							ResultUtilVO resultUtilVo = new ResultUtilVO();
							resultUtilVo.setCode(errorConfig.getOcsErrCode());
							resultUtilVo.setDescription(errorConfig.getOcsErrDesc());
							return resultUtilVo;
						}).orElseGet(() -> getDefaultError(appDto.getLang()));

			} catch (Exception e) {
				log.error("Error while getting error details", e);
				return getDefaultError(appDto.getLang());
			}

		} else {
			return getDefaultError(appDto.getLang());
		}
	}

	public static ResultUtilVO getDefaultError(String lang) 
	{
		ResultUtilVO resultUtilVo = new ResultUtilVO();
		  lang = Objects.requireNonNullElse(lang, "");
		  
		if (lang.equalsIgnoreCase("ar")) {
			resultUtilVo.setCode(AppConstant.GEN_ERROR_CODE);
			resultUtilVo.setDescription(AppConstant.GEN_ERROR_DESC_AR);
		} else if (lang.equalsIgnoreCase("fr")) {
			resultUtilVo.setCode(AppConstant.GEN_ERROR_CODE);
			resultUtilVo.setDescription(AppConstant.GEN_ERROR_DESC_FR);
		} else {
			resultUtilVo.setCode(AppConstant.GEN_ERROR_CODE);
			resultUtilVo.setDescription(AppConstant.GEN_ERROR_DESC);
		}
		return resultUtilVo;
	}

}
