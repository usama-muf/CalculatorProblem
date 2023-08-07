package com.usama.calculatorproblemv2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.usama.calculatorproblemv2.dto.RiskCalcLogicResponse;
import com.usama.calculatorproblemv2.entity.RiskCalcLogic;
import com.usama.calculatorproblemv2.entity.RiskCalcLogicCore;
import com.usama.calculatorproblemv2.service.RiskCalcLogicService;

@RestController
@RequestMapping("v1/api/rcl")
@Validated
public class RiskCalcLogicController {

    Logger log = LoggingUtils.logger;

    private final RiskCalcLogicService riskCalcLogicService;

    public RiskCalcLogicController(RiskCalcLogicService riskCalcLogicService) {
	this.riskCalcLogicService = riskCalcLogicService;
    }

    @PostMapping("/create")
    public ResponseEntity<RiskCalcLogicResponse> addNewRiskCalcLogic(@RequestBody RiskCalcLogicRequest logic) {

	RiskCalcLogicResponse response = new RiskCalcLogicResponse();

	try {
	    log.info("Received POST request with body: {}", logic);
	    boolean isFormulaPossible = riskCalcLogicService.checkFormulaValidity(logic.getFormula());

	    response.setValid(isFormulaPossible);
	    if (isFormulaPossible) {
		log.info("Formula Validated: Going forward with POST request ");
		this.riskCalcLogicService.addNewRiskCalcLogic(logic);
		response.setMessage("Submitted Successfully!!");
	    } else {
		log.error("Formula NOT Valid");
		response.setMessage("Formula Seems Incorrect, Try again.");
	    }
	    return ResponseEntity.ok(response);

	} catch (Exception e) {
	    log.error("Failed to POST data to company_risk_score tbl");
	    log.error(e.getMessage());
	    response.setMessage("Server Error !");
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

    }

    @GetMapping("/getall")
    public List<RiskCalcLogic> getAll() {

	try {
	    log.info("Received GET request to fetch all data from risk_calc_logic table");
	    List<RiskCalcLogic> calcLogic = this.riskCalcLogicService.getAll();
	    return calcLogic;

	} catch (Exception e) {
	    log.error("Failed to fetch data from company_risk_score tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();

	}
    }

    @GetMapping("/getall/core")
    public List<RiskCalcLogicCore> getAllCore() {

	try {
	    log.info("Received GET request to fetch all data from risk_calc_logic_core table");
	    List<RiskCalcLogicCore> riskLogicCore = this.riskCalcLogicService.getAllCore();
	    return riskLogicCore;

	} catch (Exception e) {
	    log.error("Failed to fetch data from Risk_calc_logic_core tbl");
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
	    log.error("Unable to delete from Risk_Calc_logic with elementName:{} ", elementName);
	    log.error(e.getMessage());
	}
	return "error";

    }

    @PutMapping("/update/{elementName}")
    public ResponseEntity<RiskCalcLogicResponse> updateRiskCalcLogic(@PathVariable String elementName,
	    @RequestBody RiskCalcLogicRequest request) {

//	log.info("Received PUT request for Risk_Calc_Logic with elementName: {} and body: {}", elementName, request);
//	this.riskCalcLogicService.updateRiskCalcLogic(elementName, request);
//
//	return "Success";

	RiskCalcLogicResponse response = new RiskCalcLogicResponse();

	try {
	    log.info("Received PUT request for FormulaName: {} with body: {}", elementName, request);
	    boolean isFormulaPossible = riskCalcLogicService.checkFormulaValidity(request.getFormula());

	    response.setValid(isFormulaPossible);
	    if (isFormulaPossible) {
		log.info("Formula Validated: Going forward with PUT request ");
		this.riskCalcLogicService.updateRiskCalcLogic(elementName, request);
		response.setMessage("Submitted Successfully!!");
	    } else {
		log.error("Formula NOT Valid");
		response.setMessage("Formula Seems Incorrect, Try again.");
	    }
	    return ResponseEntity.ok(response);

	} catch (Exception e) {
	    log.error("Failed to PUT data to company_risk_score tbl");
	    log.error(e.getMessage());
	    response.setMessage("Server Error !");
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
    }

}
