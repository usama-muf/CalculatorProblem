package com.usama.calculatorproblemv2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.entity.Output;
import com.usama.calculatorproblemv2.service.OutputService;

@RestController
@RequestMapping("/v1/api/output")
public class OutputController {
    
    private final OutputService outputService;
    Logger log = LoggingUtils.logger;

    public OutputController(OutputService outputService) {
	this.outputService = outputService;
    }
    
    @GetMapping("/calculate")
//    public ResponseEntity<OutputResponse> calculateOutput() {
	public List<Output> calculateOutput() {
//	List<Output> outputData = this.outputService.calculateOutput();
//	OutputResponse response = new OutputResponse();
//	response.setOutputs(outputData);
//	
//	return ResponseEntity.ok(response);
	
	return this.outputService.calculateOutput();
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
