package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.entity.Output;
import com.usama.calculatorproblemv2.entity.OutputParameters;
import com.usama.calculatorproblemv2.repo.OutputRepo;

@Service
public class OutputService {

    @Autowired
    private RiskCalcLogicService riskCalcLogicService;

    @Autowired
    private CompanyRiskScoreService companyRiskScoreService;

    @Autowired
    private OutputRepo outputRepo;

    private Logger log = LoggingUtils.logger;

    @Scheduled(cron = "* 0 * * * *") // Run 
    public void updateOutputTable() {

	List<String> allCompanyNames = this.companyRiskScoreService.findAllCompanyNames().stream()
		.map(company -> company.getCompanyName()).collect(Collectors.toList());
	allCompanyNames.stream().forEach(company -> {

	    // get all the output params for each company
	    log.info("Calculating output params for compnay: {}", company);
	    Map<String, Integer> outputParams = this.riskCalcLogicService.calculateFormula(company);

	    // convert the above map to Ouput entity.
	    Set<OutputParameters> set = outputParams.entrySet().stream().map(param -> {
		OutputParameters p = new OutputParameters();
		p.setParameterName(param.getKey());
		p.setParameterValue(param.getValue());
		return p;
	    })
		    // new OutputParameters(param.getKey(), param.getValue()))
		    .collect(Collectors.toSet());

	    Output output = new Output();
	    output.setCompanyName(company);
	    output.setOutputParameters(set);

	    // save all the params along with companyName in output table.
	    try {
		log.info("Successfully saving data to Output table for compnay: {}", company);
		this.outputRepo.save(output);

	    } catch (Exception e) {
		log.error("Error saving data to Ouptut table for the compnay : {}", company);
		log.error(e.getMessage());
	    }

	});

    }

    
    public  List<String> allOutputParams() {
	 return this.riskCalcLogicService.getRiskCalcLogicElementsName();
    }
    
    
    public List<Output> calculateOutput() {
	log.info("Updating Output table");

	CompletableFuture<Void> saveFuture = CompletableFuture.runAsync(this::updateOutputTable);

	try {
	    saveFuture.get(); // Wait for saveToOutputTable to complete

	    List<Output> data = this.outputRepo.findAll();
	    log.info("Successfully fetched all the data from the table 'output'");
	    return data;

	} catch (Exception e) {
	    log.error("Failed to fetch data from output tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}
    }

}
