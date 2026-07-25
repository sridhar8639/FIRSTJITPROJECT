package com.bank.retail.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bank.retail.persistence.entity.CurrencyEmbeddedId;
import com.bank.retail.persistence.entity.CurrencyMaster;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyMaster, CurrencyEmbeddedId> 
{
	
	
	
	@Query("""
		       select c
		       from CurrencyMaster c
		       where lower(c.description) = lower(?1)
		       and c.id.unit = ?2
		       """)
		Optional<CurrencyMaster> findByDescriptionAndUnitId(String description, String unitId);

}
