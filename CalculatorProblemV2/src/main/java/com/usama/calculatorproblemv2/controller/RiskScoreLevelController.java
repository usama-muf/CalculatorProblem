package com.usama.calculatorproblemv2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.usama.calculatorproblemv2.dto.RiskScoreLevelDto;
import com.usama.calculatorproblemv2.dto.RiskScoreLevelPutDto;
import com.usama.calculatorproblemv2.dto.RiskScoreLevelResponse;
import com.usama.calculatorproblemv2.entity.RiskScoreLevel;
import com.usama.calculatorproblemv2.service.RiskScoreLevelService;

@RestController
@RequestMapping("/v1/api/rsl")
@Validated
public class RiskScoreLevelController {

    private final RiskScoreLevelService riskScoreLevelService;

    Logger log = LoggingUtils.logger;

    public RiskScoreLevelController(RiskScoreLevelService riskScoreLevelService) {
	this.riskScoreLevelService = riskScoreLevelService;
    }

    @PostMapping("/create")
    public ResponseEntity<RiskScoreLevelResponse> createRiskScoreLevel(@RequestBody RiskScoreLevelDto scoreLevel) {

	log.info("Received POST request with body: {}", scoreLevel);

	RiskScoreLevelResponse response = new RiskScoreLevelResponse();

	boolean isInterfering = this.riskScoreLevelService.checkScoreValid(scoreLevel.getMinScore(),
		scoreLevel.getMaxScore(), scoreLevel.getLevel());

	log.info(isInterfering);

	response.setInterfering(!isInterfering);

	if (!isInterfering) {
	    response.setMessage("This set interferes with existing data.");
	} else {
	    response.setMessage("This set is valid and can be added.");

	    try {
		this.riskScoreLevelService.createNewRiskScoreLevel(scoreLevel);
		return ResponseEntity.ok().body(response);

	    } catch (Exception e) {
		System.out.println("This catch is running hehe");
	    }
	}
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }

    @GetMapping("getall")
    public List<RiskScoreLevel> getAllRiskScoreLevel() {

	try {
	    log.info("Received GET request for table risk_score_level");
	    List<RiskScoreLevel> findAll = this.riskScoreLevelService.getAllRiskScoreLevels();
	    log.info("Successfully fetched data from the table risk_score_level");
	    return findAll;

	} catch (Exception e) {
	    log.error("Failed to fetch data in risk_score_level tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}
    }

    @GetMapping("get/{levelName}")
    public ResponseEntity<RiskScoreLevel> getRiskScoreLevel(@PathVariable String levelName) {

	try {
	    log.info("Received GET request for table risk_score_level for Level: {}", levelName);
	    RiskScoreLevel data = this.riskScoreLevelService.getRiskScoreLevel(levelName);
	    log.info("Successfully fetched data from the table risk_score_level");
	    if (data != null)
		return ResponseEntity.ok(data);
	    else
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

	} catch (Exception e) {
	    log.error("Failed to fetch data in risk_score_level tbl");
	    log.error(e.getMessage());
	}
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RiskScoreLevel());
    }

    @PutMapping("/update/{scoreLevel}")
    public RiskScoreLevelResponse updateRiskScoreLevel(@PathVariable String scoreLevel,
	    @RequestBody RiskScoreLevelPutDto scoreDto) {

	log.info("Received Put request for {} with body: {}", scoreLevel, scoreDto);

	RiskScoreLevelResponse response = new RiskScoreLevelResponse();

	boolean isNotInterfering = this.riskScoreLevelService.updateRiskScoreLevel(scoreLevel, scoreDto);
	/*
	 * if successfully update : set IsNotInterfering true && set setInterfering :
	 * false
	 */

	System.out.println(isNotInterfering);

	response.setInterfering(!isNotInterfering);

	if (response.isInterfering()) {
	    response.setMessage("This set interferes with existing data.");
	} else {
	    response.setMessage("This set is valid and can be added.");
	}

	return response;
    }

    @DeleteMapping("/delete/{levelName}")
    public ResponseEntity<RiskScoreLevelResponse> deleteRiskScoreLevel(@PathVariable String levelName) {
	log.info("Received DELETE request for Risk_Score_Level tbl with levelName: {} ", levelName);
	RiskScoreLevelResponse response = new RiskScoreLevelResponse();

	try {
	    boolean result = this.riskScoreLevelService.deleteByLevelName(levelName);

	    if (result) {
		response.setInterfering(false);
		response.setMessage("Deleted Successfully!");
		return ResponseEntity.ok().body(response);
	    }

	} catch (Exception e) {
	    log.error("Unable to delete from Risk_Level with levelName:{} ", levelName);
	    log.error(e.getMessage());
	}
	response.setInterfering(true);
	response.setMessage("Something went Wrong. Unable to delete");
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/getUniqueLevelName")
    public ResponseEntity<List<String>> getAllUniqueLevelName() {
	try {
	    log.info("Received GET request for table risk_score_level to Fetch all Unique Level Name");
	    List<String> findAll = this.riskScoreLevelService.getAllUniqueLevelName();
	    log.info("Successfully fetched data from the table risk_score_level");
	    return ResponseEntity.ok(findAll);

	} catch (Exception e) {
	    log.error("Failed to fetch data in risk_score_level tbl");
	    log.error(e.getMessage());
	}
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

}
