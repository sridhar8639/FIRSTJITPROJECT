package com.bank.retail.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "OCS_T_MOCK_RESPONSE_DATA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockResponseData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "microservice")
    private String microservice;
    
    @Column(name = "category_code")
    private String categoryCode;
    
    @Column(name = "response_xml", columnDefinition = "TEXT")
    private String responseXml;
    
    @Column(name = "is_active")
    private Boolean isActive;
}
