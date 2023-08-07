package com.usama.calculatorproblemv2.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usama.calculatorproblemv2.entity.RiskCalcLogic;

public interface RiskCalcLogicRepo extends JpaRepository<RiskCalcLogic, Long> {
	
	public RiskCalcLogic findByElementName(String elementName);

}
