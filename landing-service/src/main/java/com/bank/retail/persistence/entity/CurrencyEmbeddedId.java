package com.bank.retail.persistence.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyEmbeddedId implements Serializable {
 
	private static final long serialVersionUID = 5176076033395796810L;
 
	@Column(name = "UNIT_ID", nullable = false, length = 3)
	private String unit;
 
	@Column(name = "CURRENCY_CODE", nullable = false, length = 3)
	private String code;

	@Column(name = "STATUS", nullable = true, length = 25, insertable=false, updatable=false)
	private String status;

}