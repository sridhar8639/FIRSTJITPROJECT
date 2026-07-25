package com.bank.retail.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseModel  implements Serializable{
	
	
	private static final long serialVersionUID = -1006426360666262669L;
	@Column(name ="STATUS")
	private String status;
	
	@CreatedBy
	@Column(name = "CREATED_BY" ,  nullable = false, updatable = false, length = 15)
	private String createdBy;
	
	@CreatedDate
	@Column(name = "DATE_CREATED", nullable = false, updatable = false)
	private LocalDateTime createdTime;
	
	@LastModifiedBy
	@Column(name = "MODIFIED_BY" ,  nullable = true, length = 15)
	private String modifiedBy;

	@LastModifiedDate
	@Column(name = "DATE_MODIFIED", nullable = true)
	private LocalDateTime modifiedTime;

}
