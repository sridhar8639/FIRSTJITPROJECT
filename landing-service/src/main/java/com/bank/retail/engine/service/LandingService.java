package com.bank.retail.engine.service;

import java.util.Map;

import com.bank.retail.api.dto.GenericResponse;

public interface LandingService 
{
	GenericResponse<Map<String, Object>> getAccountDetailsService(Map<String, String> headers,Map<String, Object> requestBody);
}
