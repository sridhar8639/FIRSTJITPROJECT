package com.bank.retail.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bank.retail.api.dto.GenericResponse;
import com.bank.retail.constants.HeaderConstants;
import com.bank.retail.engine.service.LandingService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class LandingController
{
  private final LandingService landingService;

  @PostMapping("/get-account-details")
	public GenericResponse<Map<String,Object>> getAccountDetails(
			@NotNull @NotBlank @RequestHeader(HeaderConstants.SERVICE_ID_HEADER) String serviceId,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.CHANNEL_HEADER) String channel,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.UNIT_HEADER) String unit,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.IP_ADDRESS_HEADER) String ipAddress,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.DEVICE_ID_HEADER) String deviceId,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.MODULE_ID) String moduleId,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.PARTNER_ID_HEADER) String partnerId,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.ACCEPT_LANGUAGE_HEADER) String acceptLanguage,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.SUB_MODULE_ID) String subModuleId,
			@NotNull @NotBlank @RequestHeader(HeaderConstants.SCREEN_ID) String screenId,
			 @RequestBody(required = false) Map<String, Object> requestBody) {

			Map<String, String> headers = new HashMap<>();
			headers.put(HeaderConstants.SERVICE_ID_HEADER, serviceId);
			headers.put(HeaderConstants.CHANNEL_HEADER, channel);
			headers.put(HeaderConstants.UNIT_HEADER, unit);
			headers.put(HeaderConstants.IP_ADDRESS_HEADER, ipAddress);
			headers.put(HeaderConstants.DEVICE_ID_HEADER, deviceId);
			headers.put(HeaderConstants.MODULE_ID, moduleId);
			headers.put(HeaderConstants.PARTNER_ID_HEADER, partnerId);
			headers.put(HeaderConstants.ACCEPT_LANGUAGE_HEADER, acceptLanguage);
			headers.put(HeaderConstants.SUB_MODULE_ID, subModuleId);
			headers.put(HeaderConstants.SCREEN_ID, screenId);

		return landingService.getAccountDetailsService(headers, requestBody);
	}
}
