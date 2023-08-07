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
import com.usama.calculatorproblemv2.dto.RiskCalcLogicRequest;
import com.usama.calculatorproblemv2.entity.RiskCalcLogic;
import com.usama.calculatorproblemv2.service.RiskCalcLogicService;

@RestController
@RequestMapping("v1/api/rcl")
public class RiskCalcLogicController {

    Logger log = LoggingUtils.logger;

    private final RiskCalcLogicService riskCalcLogicService;

    public RiskCalcLogicController(RiskCalcLogicService riskCalcLogicService) {
	this.riskCalcLogicService = riskCalcLogicService;
    }

    @PostMapping("/create")
    public String addNewRiskCalcLogic(@RequestBody RiskCalcLogic logic) {
	try {
	    log.info("Received POST request with body: {}", logic);
	    this.riskCalcLogicService.addNewRiskCalcLogic(logic);
	    return "Success";

	} catch (Exception e) {
	    log.error("Failed to POST data to company_risk_score tbl");
	    log.error(e.getMessage());
	    return "Error";

	}
    }

    @GetMapping("/getall")
    public List<RiskCalcLogic> getAll() {

	try {
	    log.info("Received GET request to fetch all data from risk_calc_logic table");
	    return this.riskCalcLogicService.getAll();

	} catch (Exception e) {
	    log.error("Failed to fetch data from company_risk_score tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();

	}
    }

    @DeleteMapping("/delete/{elementName}")
    public String deleteByElementName(@PathVariable String elementName) {

	log.info("Received DELETE request for Risk_Calc_logic tbl with Element_Name: {} ", elementName);

	try {
	    this.riskCalcLogicService.deleteByElementName(elementName);
	    return "Success";

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return "error";

    }

    @PutMapping("/update/{elementName}")
    public String updateRiskCalcLogic(@PathVariable String elementName, @RequestBody RiskCalcLogicRequest request) {

	log.info("Received PUT request for Risk_Calc_Logic with elementName: {} and body: {}", elementName, request);
	this.riskCalcLogicService.updateRiskCalcLogic(elementName, request);

	return "Success";
    }

}
