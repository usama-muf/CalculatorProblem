package com.usama.calculatorproblemv2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usama.calculatorproblemv2.entity.RiskDimension;

public interface RiskDimensionRepo extends JpaRepository<RiskDimension, String>{
	
    List<RiskDimension> findAllByDimensionNotNull();

}
