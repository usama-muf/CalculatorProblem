package com.usama.calculatorproblemv2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.repo.RiskDimensionRepo;

@Service
public class RiskDimensionService {
	
	private final RiskDimensionRepo riskDimensionRepo;

	public RiskDimensionService(RiskDimensionRepo riskDimensionRepo) {
		this.riskDimensionRepo = riskDimensionRepo;
	}
	
	
	public List<RiskDimension> getAllDimensionsName() {
		return riskDimensionRepo.findAllByDimensionNotNull();
	}
	
	public List<RiskDimension> getAllDimensionsAndWeight() {
		return riskDimensionRepo.findAll();
	}

	
}
