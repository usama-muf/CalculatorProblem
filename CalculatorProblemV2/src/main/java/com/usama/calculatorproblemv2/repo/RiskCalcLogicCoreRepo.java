package com.usama.calculatorproblemv2.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usama.calculatorproblemv2.entity.RiskCalcLogic;
import com.usama.calculatorproblemv2.entity.RiskCalcLogicCore;

public interface RiskCalcLogicCoreRepo extends JpaRepository<RiskCalcLogicCore, Long> {

}
