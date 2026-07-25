package com.bank.retail.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppExceptionHandlerUtilDto implements Serializable {

	private static final long serialVersionUID = 8062153858017811179L;

	private String customerId;

	private String unit;

	private String channel;

	private String mobileNumber;

	private String emailId;

	private String lang;

	private String microSerId;
	
	private String serviceId;

	private Date startTime;

	private Date endTime;
	
	private String clientInfo;
	
	private long txnCategory;
	
	private String userName;
	
	private String customerNo;
	
	private String clientSessionKey;
	
	private String sessionKey;
	
	private String mwUrl;
	
	private String moduleId;
	
	private String subModuleId;

	public AppExceptionHandlerUtilDto(String unit, String channel, String lang, String serviceId) {

		super();

		this.unit = unit;

		this.channel = channel;

		this.lang = lang;

		this.serviceId = serviceId;

	}


}