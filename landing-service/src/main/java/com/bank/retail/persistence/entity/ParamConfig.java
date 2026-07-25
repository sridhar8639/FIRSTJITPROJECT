package com.bank.retail.persistence.entity;

import com.bank.retail.constants.TableConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = TableConstants.TABLE_CONFIG)
@Data
public class ParamConfig extends BaseModel{
    
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "TXN_ID")
    private Long txnId;
    
    @Column(name = "REMARKS", length = 1000)
    private String remarks;
    
    @Column(name = "CONF_KEY", length = 200, nullable = false)
    private String confKey;
    
    @Column(name = "STATUS", length = 20, nullable = false)
    private String status;
    
    @Column(name = "CONF_VALUE", length = 200, nullable = false)
    private String confValue;
    
    @Column(name = "CHANNEL_ID", length = 3, nullable = false)
    private String channelId;
    
    @Column(name = "UNIT_ID", length = 3)
    private String unitId;
    
}