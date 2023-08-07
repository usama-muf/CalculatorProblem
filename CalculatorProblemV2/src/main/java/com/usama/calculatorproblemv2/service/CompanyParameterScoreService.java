package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.Collections;
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
import com.usama.calculatorproblemv2.entity.CompanyParameterScore;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.entity.RiskParameters;
import com.usama.calculatorproblemv2.repo.CompanyParameterScoreRepo;
import com.usama.calculatorproblemv2.repo.RiskDimensionRepo;

import jakarta.transaction.Transactional;

@Service
//@Slf4j
public class CompanyParameterScoreService {

    private final CompanyParameterScoreRepo companyRiskScoreRepo;

//    private final OutputService outputService;

    private final RiskDimensionRepo riskDimensionRepo;

    Logger log = LoggingUtils.logger;

//    public CompanyRiskScoreService() {
//    }

    public CompanyParameterScoreService(CompanyParameterScoreRepo companyRiskScoreRepo, /* OutputService outputService, */
	    RiskDimensionRepo riskDimensionRepo) {
	this.companyRiskScoreRepo = companyRiskScoreRepo;
//	this.outputService = outputService;
	this.riskDimensionRepo = riskDimensionRepo;
    }

    @Transactional
    public String createNewRiskScoreRow(CompanyRiskScoreRequest request) {

	CompanyParameterScore companyRiskScore = new CompanyParameterScore();

	String companyName = request.getCompanyName();

//	for (Map<String, Integer> map : request.getParameters()) {

//	    for (Map.Entry<String, Integer> entry : map.entrySet()) {
	for (Map.Entry<String, Integer> entry : request.getParameters().entrySet()) {

	    RiskParameters riskParameters = new RiskParameters();

	    // validating ParameterName and Value.
	    riskParameters.setParameterName(entry.getKey());
	    riskParameters.setParameterValue(entry.getValue());
	    companyRiskScore.setCompanyName(companyName);
	    companyRiskScore.getParameters().add(riskParameters);
	}
//	}
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
	log.info("Updating RiskDimensionTbl");
	// Add unique parameters to risk_Dimension Table with initial Value as 0
	// Get all unique parameters from the Company_risk_score table and adding weight
	// before them, so that it can match with dimensions table.

	List<String> uniqueRiskScoreParameters = findUniqueParametersName().stream().map(str -> "weight_" + str)
		.collect(Collectors.toList());
	log.info(uniqueRiskScoreParameters);

	// Get all unique dimensions from the Risk_dimension table.
	List<String> riskDimensionDimensions = riskDimensionRepo.findAll().stream().map(d -> d.getDimension())
		.collect(Collectors.toList());

	log.info(riskDimensionDimensions);
	// compare Both the arraylist
	boolean areListsEqual = areArrayListsEqual(uniqueRiskScoreParameters, riskDimensionDimensions);
	log.info("Are array list equal ? {}", areListsEqual);

	// 1) if they are same then no changes needed.
	if (areListsEqual)
	    return;

	// 2) if not same : then 2 conditions
	// deleted some attributes in a company (uniqueRiskScoreParameters<
	// riskDimensionDimensions)
	// added new attributes in a company (uniqueRiskScoreParameters >
	// riskDimensionDimensions)
	else {
	    if (uniqueRiskScoreParameters.size() < riskDimensionDimensions.size()) {
		// deleted attributes
		List<String> dimensionsToDelete = riskDimensionDimensions.stream()
			.filter(element -> !uniqueRiskScoreParameters.contains(element)).collect(Collectors.toList());

		log.info("uniqueRiskScoreParameters.size() < riskDimensionDimensions.size() delete list: {}",
			dimensionsToDelete);
		deleteDimensionMethod(dimensionsToDelete);
		return;
	    }

	    else {
		List<String> dimensionsToAdd = uniqueRiskScoreParameters.stream()
			.filter(element -> !riskDimensionDimensions.contains(element)).collect(Collectors.toList());

		log.info(
			"uniqueRiskScoreParameters.size() > riskDimensionDimensions.size() creae new dimension list: {}",
			dimensionsToAdd);
		createDimensionMethod(dimensionsToAdd);
		return;
	    }
	}

//	System.out.println("\nDimensions in riskDimension tbl " + riskDimensionDimensions);
//	System.out.println("\nUnique Params name  in Company risk score table tbl " + uniqueRiskScoreParameters);
//
//	// Check from these new parameters which one are not saved in Risk_Dimension
//	List<String> dimensionsToSave = new ArrayList<>(uniqueRiskScoreParameters);
//	dimensionsToSave.removeAll(riskDimensionDimensions);
//	System.out.println("Dimensions to save all current " + dimensionsToSave);
//	List<String> dimensionsToDelete = new ArrayList<>(riskDimensionDimensions);
//	dimensionsToDelete.removeAll(uniqueRiskScoreParameters);
//	System.out.println("Dimensions to Delete " + dimensionsToDelete);

	// save/delete the above parameter to risk_Dimension table with initial value 0
//	createDimensionMethod(dimensionsToSave);

//	deleteDimensionMethod(dimensionsToDelete);
    }

    /**
     * @param dimensionsToSave
     */
    private void createDimensionMethod(List<String> dimensionsToSave) {
	dimensionsToSave.stream().forEach(dimension -> {
	    RiskDimension riskDimension = new RiskDimension();
	    riskDimension.setDimension(dimension);
	    riskDimension.setWeight(0);

	    try {
		this.riskDimensionRepo.save(riskDimension);

	    } catch (Exception e) {
		log.info("Unable to save company parameter to Risk_dimension tbl: {}", dimension);
		log.info(e.getMessage());
	    }

	});
    }

    /**
     * @param dimensionsToDelete
     */
    private void deleteDimensionMethod(List<String> dimensionsToDelete) {
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

    public static <T extends Comparable<? super T>> boolean areArrayListsEqual(List<String> uniqueRiskScoreParameters,
	    List<String> riskDimensionDimensions) {
	// If the sizes of the two ArrayLists are different, they are not equal
	if (uniqueRiskScoreParameters.size() != riskDimensionDimensions.size()) {
	    return false;
	}

	// Sort the lists to ensure elements are in the same order
	Collections.sort(uniqueRiskScoreParameters);
	Collections.sort(riskDimensionDimensions);

	// Check each element in the two ArrayLists
	for (int i = 0; i < uniqueRiskScoreParameters.size(); i++) {
	    if (!uniqueRiskScoreParameters.get(i).equals(riskDimensionDimensions.get(i))) {
		return false;
	    }
	}

	// If all elements are equal, return true
	return true;
    }

    public List<String> findUniqueParametersName() {
	return companyRiskScoreRepo.findUniqueParametersName();
    }

    public List<CompanyParameterScore> findAll() {

	try {
	    List<CompanyParameterScore> findAll = this.companyRiskScoreRepo.findAll();
	    log.info("Successfully Fetched data from the database company_risk_score");
	    updateRiskDimensionTable();
	    return findAll;

	} catch (Exception e) {
	    log.error("Failed to fetch data from company_risk_score tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();

	}
    }

    public void deleteByCompanyName(String companyName) {
	CompanyParameterScore crs = findByCompanyName(companyName);

	try {
	    this.companyRiskScoreRepo.delete(crs);
	    log.info("Successfully DELETED from the database company_risk_score with companyName: {}", companyName);
	    log.info("Initiated Delete form Output table based on companyName:{}", companyName);
//	    this.outputService.deleteByCompanyName(companyName);

	} catch (Exception e) {
	    log.error("Failed to delete from company_risk_score db with companyName: {}", companyName);
	    log.error(e.getMessage());
	}

    }

    public CompanyParameterScore findByCompanyName(String companyName) {

	log.info("Fetching data from Company_Risk_Score tbl for companyName: {}", companyName);

	CompanyParameterScore crs = this.companyRiskScoreRepo.findById(companyName).orElseThrow(() -> {
	    log.error("No company found with companyName: {} in company_risk_score tbl", companyName);
	    return new RuntimeException(companyName + " not Found");
	});
	return crs;
    }

    public void updateCompanyRiskScore(String companyName, CompanyRiskScorePutRequest request) {

	Optional<CompanyParameterScore> companyData = companyRiskScoreRepo.findById(companyName);
	CompanyParameterScore companyRiskScore = new CompanyParameterScore();

	// setting the response to pattern so that it can be saved in db
	Set<RiskParameters> riskParameters = request.getParameters().entrySet().stream().map(entry -> {
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

		updateRiskDimensionTable();

	    } catch (Exception e) {
		log.error("Failed to UPDATE in company_risk_score db with companyName: {}", companyName);
		log.error(e.getMessage());
	    }
	} else {
	    log.error("Failed to Find in company_risk_score db with companyName: {}", companyName);

	}

    }

    public void createNewCompanyRiskScoreParameter(String companyName, CompanyRiskScorePutRequest request) {

	Optional<CompanyParameterScore> companyData = companyRiskScoreRepo.findById(companyName);

	if (companyData.isPresent()) {
	    CompanyParameterScore companyRiskScore = companyData.get();
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
