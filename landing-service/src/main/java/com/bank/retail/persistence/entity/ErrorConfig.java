package com.bank.retail.persistence.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OCS_T_ERROR_CONFIG")
@Entity
public class ErrorConfig implements Serializable {

	private static final long serialVersionUID = 1149816592164550986L;

	@Id
	@Column(name = "TXN_ID")
	private int txnId;

	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "LANG_CODE")
	private String lang;

	@Column(name = "CHANNEL_ID")
	private String channelId;

	@Column(name = "SERVICE_TYPE")
	private String serviceType;

	@Column(name = "MW_ERR_CODE")
	private String mwErrorCode;

	@Column(name = "OCS_ERR_CODE")
	private String ocsErrCode;

	@Column(name = "OCS_ERR_DESC")
	private String ocsErrDesc;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "REMARKS")
	private String remarks;

}
