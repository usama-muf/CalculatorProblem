package com.usama.calculatorproblemv2.controller;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.usama.calculatorproblemv2.dto.RiskDimensionResponse;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.service.RiskDimensionService;

@RestController
@RequestMapping("/v1/api/rd")
@Validated
public class RiskDimensionController {

    @Autowired
    private RiskDimensionService riskDimensionService;

    Logger log = LoggingUtils.logger;

    @GetMapping("/alldimensionnames")
    public ResponseEntity<List<String>> getAllDimensionsName() {
	return ResponseEntity.ok(this.riskDimensionService.getAllDimensionsName());
    }

    @GetMapping("/dimension-wt")
    public ResponseEntity<List<RiskDimension>> getAllDimensionsAndWeight() {
	return ResponseEntity.ok(this.riskDimensionService.getAllDimensionsAndWeight());
    }

    @PostMapping("/create")
    public ResponseEntity<RiskDimension> createNewRiskDimension(@RequestBody RiskDimension request) {
	log.info("Received POST request for Risk_Dimension tbl with Body: {} ", request);

	try {
	    RiskDimension data = this.riskDimensionService.createNewRiskDimension(request);
	    return ResponseEntity.ok(data);

	} catch (Exception e) {
	    log.error("Unable to Post in Risk_Calc_logic with body:{} ", request);
	    log.error(e.getMessage());
	    ResponseEntity.internalServerError().build();
	}
	return null;
    }

    @DeleteMapping("/delete/{dimensionName}")
    public ResponseEntity<?> deleteByDimensionName(@PathVariable String dimensionName) {

	log.info("Received DELETE request for Risk_Dimension tbl with Dimension_Name: {} ", dimensionName);

	try {
	    this.riskDimensionService.deleteByDimensionName(dimensionName);
	    return ResponseEntity.ok("Success");

	} catch (Exception e) {
	    log.error("Unable to delete from Risk_Calc_logic with elementName:{} ", dimensionName);
	    log.error(e.getMessage());
	}
	return ResponseEntity.ok("Error");

    }

    @PutMapping("/update/{dimensionName}")
    public ResponseEntity<RiskDimensionResponse> updateRiskDimension(@PathVariable String dimensionName,
	    @RequestBody RiskDimension request) {

	log.info("Received Update request for Risk_Dimension tbl with Dimension_Name: {} with body  {}", dimensionName, request);
	try {
	    return ResponseEntity.ok(this.riskDimensionService.updateRiskDimension(dimensionName, request));

	} catch (Exception e) {
	    log.error("Unable to delete from Risk_Calc_logic with elementName:{} ", dimensionName);
	    log.error(e.getMessage());
	}
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		.body(new RiskDimensionResponse(false, "Internal Server Error"));
    }

}
