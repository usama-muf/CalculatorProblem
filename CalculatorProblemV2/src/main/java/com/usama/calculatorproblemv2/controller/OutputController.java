package com.usama.calculatorproblemv2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.entity.Output;
import com.usama.calculatorproblemv2.service.OutputService;

@RestController
@RequestMapping("/v1/api/output")
@Validated
public class OutputController {

    private final OutputService outputService;
    Logger log = LoggingUtils.logger;

    public OutputController(OutputService outputService) {
	this.outputService = outputService;
    }

    @GetMapping("/calculate")
    public List<Output> getAllOutput() {
	return this.outputService.calculateOutput();
    }

    
    @GetMapping("/trigger")
  public ResponseEntity<List<Output>> calculateOutput() {

	log.info("Received GET request to Trigger recalculation of output and  fetch all Outputs");

	this.outputService.updateOutputTable();
	return ResponseEntity.ok(getAllOutput());
  }
    
    @GetMapping("/outputTblParams")
    public List<String> outputTableParams() {

	log.info("Received GET request to fetch all Output table Params");
	try {
	    return this.outputService.allOutputParams();
	} catch (Exception e) {
	    log.error("Unable to Process GET request to fetch all Output table Params");
	    log.error(e.getMessage());

	    return new ArrayList<>();
	}

    }

}
