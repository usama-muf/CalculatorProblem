package com.usama.calculatorproblemv2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usama.calculatorproblemv2.entity.CompanyRiskScore;

public interface CompanyRiskScoreRepo extends JpaRepository<CompanyRiskScore, String> {
 
	@Query("SELECT DISTINCT rp.parameterName FROM CompanyRiskScore crs JOIN crs.parameters rp")
	public List<String> findUniqueParametersName();

	
	@Query("SELECT rp.parameterName, rp.parameterValue FROM CompanyRiskScore crs JOIN crs.parameters rp where crs.companyName like CONCAT('%', :companyName, '%')")
	public List<Object[]> findByCompanyName(String companyName);


//	@Query("SELECT crs.companyName FROM CompnayRiskScore crs JOIN crs.parameters")
//	public List<Object[]> findAllCompanyNames();
}
