package com.bank.retail.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "OCS_T_CURRENCY_MASTER")
public class CurrencyMaster extends BaseModel {

	private static final long serialVersionUID = -8032944473314781424L;

	@EmbeddedId
	private CurrencyEmbeddedId id;

	@Column(name = "CURRENCY_ISO_CODE", nullable = false, length = 3)
	private String isoCode;

	@Column(name = "QCB_CURRENCY_CODE")
	private Integer qcbCode;

	@Column(name = "SPOT_RATE_RECIPROCAL")
	private String spotRateReciprocal;

	@Column(name = "NO_OF_DECIMALS")
	private Integer decimal;

	@Column(name = "DESCRIPTION", nullable = true, length = 1000)
	private String description;

	@Column(name = "LANG", nullable = false, length = 5)
	private String langType;
	

}
