package com.bank.retail.api.dto;

import com.bank.retail.constants.AppConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResultUtilVO {

	private String code;
	private String description;

	// Static factory methods for common statuses
	public static ResultUtilVO success() {
		return new ResultUtilVO("000000", "SUCCESS");
	}

	public static ResultUtilVO error(String code, String description) {
		return new ResultUtilVO(code, description);
	}

	public static ResultUtilVO error(String description) {
		return new ResultUtilVO("999999", description);
	}

	public static ResultUtilVO error() {
		return new ResultUtilVO(AppConstant.GEN_ERROR_CODE, AppConstant.GEN_ERROR_DESC);
	}
}
