package com.usama.calculatorproblemv2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.CompanyRiskScorePutRequest;
import com.usama.calculatorproblemv2.dto.CompanyRiskScoreRequest;
import com.usama.calculatorproblemv2.entity.CompanyRiskScore;
import com.usama.calculatorproblemv2.service.CompanyRiskScoreService;

@RestController
@RequestMapping("/v1/api/crs")
public class CompanyRiskScoreController {

    Logger log = LoggingUtils.logger;

    private final CompanyRiskScoreService companyRiskScoreService;

    public CompanyRiskScoreController(CompanyRiskScoreService companyRiskScoreService) {
	this.companyRiskScoreService = companyRiskScoreService;
    }

    @PostMapping("/create")
    public String createNewRiskScoreRow(@RequestBody CompanyRiskScoreRequest request) {

	log.info("Received POST request for New_Company_Risk_Score_Row with body: {}", request);
	return companyRiskScoreService.createNewRiskScoreRow(request);

    }

    @GetMapping("/findUniqueParamsName")
    public List<String> findUniqueParametersName() {

	log.info("Received GET request to fetch Unique Params in Company_Risk_Score table");
	return companyRiskScoreService.findUniqueParametersName();
    }

    @Deprecated
    @GetMapping("/findByCompanyName/{companyName}")
    public List<Object[]> findByCompanyName(@PathVariable String companyName) {

	return companyRiskScoreService.findByCompanyName(companyName);
    }

    @DeleteMapping("/delete/{companyName}")
    public String deleteByCompanyName(@PathVariable String companyName) {

	log.info("Received DELETE request for Company_Risk_Score with companyName: {} ", companyName);

	try {
	    this.companyRiskScoreService.deleteByCompanyName(companyName);
	    return "Success";

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return "error";

    }

    @GetMapping("/get/{companyName}")
    public CompanyRiskScore findByCompanyNameBase(@PathVariable String companyName) {

	log.info("Received GET request for Company_Risk_Score with companyName: {} ", companyName);
	return this.companyRiskScoreService.findByCompanyNameBase(companyName);
    }

    @GetMapping("/getall")
    public List<CompanyRiskScore> getAllCompanyData() {
	log.info("Received Get Reqeust to fetch all data from the table company_risk_score");
	try {
	    return this.companyRiskScoreService.findAllCompanyNames();
	    
	} catch (Exception e) {
	    log.error("Enable to process request");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}
    }

    // TODO
    @PutMapping("/update/{companyName}")
    public String updateCompanyRiskScore(@PathVariable String companyName,
	    @RequestBody CompanyRiskScorePutRequest request) {

	log.info("Received PUT request for New_Company_Risk_Score_Row with companyName: {} and body: {}", companyName,
		request);
	this.companyRiskScoreService.updateCompanyRiskScore(companyName, request);

	return "Success";
    }
    
    @PutMapping("/create/parameter/{companyName}")
    public String createNewCompanyRiskScoreParameter(@PathVariable String companyName,
	    @RequestBody CompanyRiskScorePutRequest request) {

	log.info("Received PUT request to add a new Parameter for New_Company_Risk_Score_Row with companyName: {} and body: {}", companyName,
		request);
	this.companyRiskScoreService.updateCompanyRiskScore(companyName, request);

	return "Success";
    }
    

}
