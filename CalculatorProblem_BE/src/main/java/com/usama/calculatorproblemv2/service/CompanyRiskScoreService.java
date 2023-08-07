package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.CompanyRiskScorePutRequest;
import com.usama.calculatorproblemv2.dto.CompanyRiskScoreRequest;
import com.usama.calculatorproblemv2.entity.CompanyRiskScore;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.entity.RiskParameters;
import com.usama.calculatorproblemv2.repo.CompanyRiskScoreRepo;
import com.usama.calculatorproblemv2.repo.RiskDimensionRepo;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyRiskScoreService {


     @Autowired
    private RiskCalcLogicService riskCalcLogicService;

    @Autowired
    private CompanyRiskScoreService companyRiskScoreService;
    Logger log = LoggingUtils.logger;

    @Transactional
    public String createNewRiskScoreRow(CompanyRiskScoreRequest request) {

	CompanyRiskScore companyRiskScore = new CompanyRiskScore();

	String companyName = request.getCompanyName();

	for (Map<String, Integer> map : request.getParameters()) {

	    for (Map.Entry<String, Integer> entry : map.entrySet()) {

		RiskParameters riskParameters = new RiskParameters();
		riskParameters.setParameterName(entry.getKey());
		riskParameters.setParameterValue(entry.getValue());
		companyRiskScore.setCompanyName(companyName);
		companyRiskScore.getParameters().add(riskParameters);
	    }
	}
	try {
	    companyRiskScoreRepo.save(companyRiskScore);
	    log.info("Successfully saved request in the database company_risk_score: {}", companyRiskScore);

	    updateRiskDimensionTable();
	    log.info("Initiated Risk_Dimension_Table updation based on new entries in company_risk_score table");

	    return "Table created successfully.";

	} catch (Exception e) {
	    log.error("Failed to save request in the database company_risk_score: {}", companyRiskScore);
	    log.error(e.getMessage());
	    return "Error creating table.";
	}

    }

    /**
     * This method is updating Risk_Dimension table i.e, if we got any new company
     * parameters then it will add this new parameter to risk_dimension table. and
     * vice versa.
     */
    private void updateRiskDimensionTable() {
	// Add unique parameters to risk_Dimension Table with initial Value as 0
	// Get all unique parameters from the Company_risk_score table.
	List<String> uniqueRiskScoreParameters = findUniqueParametersName();

	// Get all unique dimensions from the Risk_dimension table.
	List<String> riskDimensionDimensions = riskDimensionRepo.findAllByDimensionNotNull().stream()
		.map(d -> d.getDimension()).collect(Collectors.toList());

	// Check from these new parameters which one are not saved in Risk_Dimension
	List<String> dimensionsToSave = new ArrayList<>(uniqueRiskScoreParameters);
	dimensionsToSave.removeAll(riskDimensionDimensions);
	List<String> dimensionsToDelete = new ArrayList<>(riskDimensionDimensions);
	dimensionsToDelete.removeAll(uniqueRiskScoreParameters);

	// save/delete the above parameter to risk_Dimension table with initial value 0
	dimensionsToSave.stream().forEach(dimension -> {
	    RiskDimension riskDimension = new RiskDimension();
	    riskDimension.setDimension(dimension);
	    riskDimension.setWeight((int) (Math.random() * 100));

	    try {
		this.riskDimensionRepo.save(riskDimension);

	    } catch (Exception e) {
		log.info("Unable to save company parameter to Risk_dimension tbl: {}", dimension);
		log.info(e.getMessage());
	    }

	});

	dimensionsToDelete.stream().forEach(dimension -> {
	    RiskDimension riskDimension = this.riskDimensionRepo.findById(dimension)
		    .orElseThrow(() -> new RuntimeException(
			    "Dimension Not found for id: " + dimension + " in the table RiskDeminsion"));

	    try {
		this.riskDimensionRepo.delete(riskDimension);

	    } catch (Exception e) {
		log.info("Unable to Delete company parameter from Risk_dimension tbl: {}", dimension);
		log.info(e.getMessage());
	    }

	});
    }

    public List<String> findUniqueParametersName() {
	return companyRiskScoreRepo.findUniqueParametersName();
    }

    @Deprecated
    public List<Object[]> findByCompanyName(String companyName) {
	List<Object[]> companyParameter = companyRiskScoreRepo.findByCompanyName(companyName);
	return companyParameter;

    }

    public List<CompanyRiskScore> findAllCompanyNames() {

	try {
	    List<CompanyRiskScore> findAll = this.companyRiskScoreRepo.findAll();
	    log.info("Successfully Fetched data from the database company_risk_score");
	    return findAll;

	} catch (Exception e) {
	    log.error("Failed to fetch data from company_risk_score tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();

	}
    }

    public void deleteByCompanyName(String companyName) {
	CompanyRiskScore crs = findByCompanyNameBase(companyName);

	try {
	    this.companyRiskScoreRepo.delete(crs);
	    log.info("Successfully DELETED from the database company_risk_score with companyName: {}", companyName);

	} catch (Exception e) {
	    log.error("Failed to delete from company_risk_score db with companyName: {}", companyName);
	    log.error(e.getMessage());
	}

    }

    public CompanyRiskScore findByCompanyNameBase(String companyName) {
	CompanyRiskScore crs = this.companyRiskScoreRepo.findById(companyName).orElseThrow(() -> {
	    log.error("No company found with companyName: {} in company_risk_score tbl", companyName);
	    return new RuntimeException(companyName + " not Found");
	});
	return crs;
    }

    public void updateCompanyRiskScore(String companyName, CompanyRiskScorePutRequest response) {

	Optional<CompanyRiskScore> companyData = companyRiskScoreRepo.findById(companyName);
	CompanyRiskScore companyRiskScore = new CompanyRiskScore();

	// setting the response to pattern so that it can be saved in db
	Set<RiskParameters> riskParameters = response.getParameters().entrySet().stream().map(entry -> {
	    RiskParameters parameter = new RiskParameters();
	    parameter.setParameterName(entry.getKey());
	    parameter.setParameterValue(entry.getValue());
	    return parameter;
	}).collect(Collectors.toSet());

	if (companyData.isPresent()) {
	    companyRiskScore.setCompanyName(companyName);
	    companyRiskScore.setParameters(riskParameters);

	    try {
		this.companyRiskScoreRepo.save(companyRiskScore);
		log.info("Successfully UPDATED in the database company_risk_score with companyName: {}, and body: {}",
			companyName, companyRiskScore);

	    } catch (Exception e) {
		log.error("Failed to UPDATE in company_risk_score db with companyName: {}", companyName);
		log.error(e.getMessage());
	    }
	} else {
	    log.error("Failed to Find in company_risk_score db with companyName: {}", companyName);

	}

    }

    public void createNewCompanyRiskScoreParameter(String companyName, CompanyRiskScorePutRequest request) {
	
	Optional<CompanyRiskScore> companyData = companyRiskScoreRepo.findById(companyName);
	
	if (companyData.isPresent()) {
	    CompanyRiskScore companyRiskScore = companyData.get();
	    companyRiskScore.setCompanyName(companyName);
	    
//	    companyRiskScore.setParameters(companyRiskScore.getParameters().add({
//		RiskParameters riskParameters = new RiskParameters();
////		riskParameters.setParameterName(request.)
//	    }));

	    try {
		this.companyRiskScoreRepo.save(companyRiskScore);
		log.info("Successfully UPDATED in the database company_risk_score with companyName: {}, and body: {}",
			companyName, companyRiskScore);

	    } catch (Exception e) {
		log.error("Failed to UPDATE in company_risk_score db with companyName: {}", companyName);
		log.error(e.getMessage());
	    }
	} else {
	    log.error("Failed to Find in company_risk_score db with companyName: {}", companyName);

	}
	
	
    }

}
