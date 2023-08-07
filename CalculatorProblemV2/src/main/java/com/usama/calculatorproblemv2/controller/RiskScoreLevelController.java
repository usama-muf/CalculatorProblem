package com.usama.calculatorproblemv2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.entity.RiskScoreLevel;
import com.usama.calculatorproblemv2.repo.RiskScoreLevelRepo;

@RestController
@RequestMapping("/v1/api/rsl")
public class RiskScoreLevelController {

    private final RiskScoreLevelRepo riskScoreLevelRepo;

    Logger log = LoggingUtils.logger;

    public RiskScoreLevelController(RiskScoreLevelRepo riskScoreLevelRepo) {
	this.riskScoreLevelRepo = riskScoreLevelRepo;
    }

    @PostMapping("/create")
    public RiskScoreLevel createRiskScoreLevel(@RequestBody RiskScoreLevel scoreLevel) {

	try {
	    log.info("Received POST request with body: {}", scoreLevel);
	    RiskScoreLevel savedScoreLevel = riskScoreLevelRepo.save(scoreLevel);
	    log.info("Successfully Saved data in the table risk_score_level");
	    return savedScoreLevel;

	} catch (Exception e) {
	    log.error("Failed to POST data in risk_score_level tbl");
	    log.error(e.getMessage());
	    return null;
	}

    }

    @GetMapping("getall")
    public List<RiskScoreLevel> getAllRiskScoreLevel() {
	
	try {
	    log.info("Received GET request for table risk_score_level" );
	    List<RiskScoreLevel> findAll = riskScoreLevelRepo.findAll();
	    log.info("Successfully fetched data from the table risk_score_level");
	    return findAll;


	} catch (Exception e) {
	    log.error("Failed to fetch data in risk_score_level tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}
    }
}
