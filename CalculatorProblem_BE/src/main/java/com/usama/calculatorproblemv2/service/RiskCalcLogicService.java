package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.RiskCalcLogicRequest;
import com.usama.calculatorproblemv2.entity.RiskCalcLogic;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.repo.RiskCalcLogicRepo;
import com.usama.calculatorproblemv2.repo.RiskScoreLevelRepo;
import com.usama.calculatorproblemv2.repo.ScoreCapRepo;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

@Service
public class RiskCalcLogicService {
    @Autowired
    private CompanyRiskScoreService companyRiskScoreService;
    @Autowired
    private RiskDimensionService riskDimensionService;
    @Autowired
    private RiskCalcLogicRepo riskCalcLogicRepo;
    @Autowired
    private RiskScoreLevelRepo riskScoreLevelRepo;
    @Autowired
    private ScoreCapRepo scoreCapRepo;

    Logger log = LoggingUtils.logger;

    public Map<String, Integer> getCompanysParameterAndScoreData(String companyName) {
	List<Object[]> parameters = this.companyRiskScoreService.findByCompanyName(companyName);

	Map<String, Integer> companyParameterScore = parameters.stream()
		.collect(Collectors.toMap(entry -> (String) entry[0], entry -> (Integer) entry[1]));

//		printEachValueOfMap(companyParameterScore);
	return companyParameterScore;
    }

    public Map<String, Integer> getRiskDimensionAndWeight() {
	List<RiskDimension> riskDimensonValues = this.riskDimensionService.getAllDimensionsAndWeight();

	Map<String, Integer> riskDimensionAndWeight = riskDimensonValues.stream()
		.collect(Collectors.toMap(entry -> entry.getDimension() + " Weight", entry -> entry.getWeight()));

//		printEachValueOfMap(riskDimensionAndWeight);
	return riskDimensionAndWeight;

    }

    public void addNewRiskCalcLogic(RiskCalcLogic request) {

	try {
	    this.riskCalcLogicRepo.save(request);
	    log.info("Data saved Successfully");

	} catch (Exception e) {
	    log.error("Error saving data in risk_cal_logic");
	}
    }

    public List<String> getRiskCalcLogicElementsName() {
	try {
	    List<RiskCalcLogic> allData = this.riskCalcLogicRepo.findAll();
	    List<String> allElementsName = allData.stream().map(data -> data.getElementName())
		    .collect(Collectors.toList());
	    log.info("Fetching all the ElementName in risk_calc_logic tbl");
	    return allElementsName;

	} catch (Exception e) {

	    log.error("Error fetching ElementName from risk_calc_logic");
	    log.error(e.getMessage());
	    return new ArrayList<>();
	}
    }

//	public void printEachValueOfMap(Map<String, Integer> map) {
//		map.forEach((name, value) -> System.out.println(name + ": " + value));
//
//	}

    public void calculateOutput() {
//		List<String> allCompanyName = this.companyRiskScoreService.findAllCompanyNames();
	List<String> allCompanyNames = this.companyRiskScoreService.findAllCompanyNames().stream()
		.map(company -> company.getCompanyName()).collect(Collectors.toList());
	allCompanyNames.stream().forEach(this::calculateFormula);
//		calculateFormula();

    }

    public Map<String, Integer> calculateFormula(String companyName) {

	List<RiskCalcLogic> allFormulas = this.riskCalcLogicRepo.findAll();

	Map<String, Integer> riskCalcLogicOutput = new HashMap<>();

	for (RiskCalcLogic riskCalcLogic : allFormulas) {

	    String elementName = riskCalcLogic.getElementName();
	    String formula = riskCalcLogic.getFormula();
	    getEachElementNameValue(elementName, formula, companyName, riskCalcLogicOutput);

	}

	// Hardcoding this function
	calculateTotalRiskScore(riskCalcLogicOutput);
	calculateTotalRiskCappedScore(riskCalcLogicOutput, companyName);
	calculateCompositeRiskScore(riskCalcLogicOutput);

//	System.out.println("\n CompnayName --> " + companyName + "  <-->  " + riskCalcLogicOutput + "\n");

	return riskCalcLogicOutput;

    }

// TODO: Hardcoding elementName.
    private void calculateCompositeRiskScore(Map<String, Integer> riskCalcLogicOutput) {

	String elementName = "composite_risk_score";
	String formula = this.riskCalcLogicRepo.findByElementName(elementName).getFormula();

	for (Map.Entry<String, Integer> entry : riskCalcLogicOutput.entrySet()) {
	    String outputKey = entry.getKey();
	    Integer outputValue = entry.getValue();
	    formula = formula.replaceFirst(outputKey, Integer.toString(outputValue));
	}

	Function minFunction = new Function("MIN", 2) {
	    @Override
	    public double apply(double... args) {
		return Math.min(args[0], args[1]);
	    }
	};

	try {
	    Expression expression = new ExpressionBuilder(formula).functions(minFunction).build();
	    double result = expression.evaluate();
	    riskCalcLogicOutput.put(elementName, (int) result);

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void calculateTotalRiskCappedScore(Map<String, Integer> riskCalcLogicOutput, String companyName) {

	// Getting all parameters of companyName
	Map<String, Integer> companysParameterAndScoreData = getCompanysParameterAndScoreData(companyName);

	// this map stores the Level(risk Score level) and its count
	Map<String, Integer> levelCtr = new HashMap<>();

	// loop through each parameter and find its Level using riskScoreLevel table.
	for (Map.Entry<String, Integer> entry : companysParameterAndScoreData.entrySet()) {

	    String levelByScore = this.riskScoreLevelRepo.findLevelByScore(entry.getValue());

	    // store this levelByScore in a Map with its count.
	    levelCtr.put(levelByScore, levelCtr.getOrDefault(levelByScore, 0) + 1);
	}

	// using this levelCtr map and score_cap table find the totalRiskCappedScore.
	Integer finalRiskCappedScore = Integer.MAX_VALUE;
	for (Map.Entry<String, Integer> entry : levelCtr.entrySet()) {
	    String level = entry.getKey();
	    Integer counter = entry.getValue();
	    Integer totalRiskCappedScore = this.scoreCapRepo.findTotalRiskCappedScore(level, counter);
	    finalRiskCappedScore = Math.min(finalRiskCappedScore, totalRiskCappedScore);
	}

	riskCalcLogicOutput.put("total_risk_capped_score", finalRiskCappedScore);

    }

    // hardcoded.
    private void calculateTotalRiskScore(Map<String, Integer> riskCalcLogicOutput) {

	// fetch this from db
	String elementName = "total_risk_score";
	String formula = "information_security_weight + resilience_weight + conduct_weight";

	for (Map.Entry<String, Integer> entry : riskCalcLogicOutput.entrySet()) {
	    String key = entry.getKey();
	    Integer value = entry.getValue();

	    formula = formula.replaceFirst(key, Integer.toString(value));
	}

	try {
	    Expression expression = new ExpressionBuilder(formula).build();

	    Integer value = (int) expression.evaluate();
	    riskCalcLogicOutput.put(elementName, value);

	} catch (Exception e) {
	    e.printStackTrace();

	}

    }

    private void getEachElementNameValue(String elementName, String formula, String companyName,
	    Map<String, Integer> riskCalcLogicOutput) {

	Map<String, Integer> riskDimensionAndWeight = getRiskDimensionAndWeight();
	Map<String, Integer> companysParameterAndScoreData = getCompanysParameterAndScoreData(companyName);

	for (Map.Entry<String, Integer> entry : companysParameterAndScoreData.entrySet()) {
	    String key = entry.getKey();
	    Integer value = entry.getValue();

	    formula = formula.replaceFirst(key, Integer.toString(value));
	}
	for (Map.Entry<String, Integer> entry : riskDimensionAndWeight.entrySet()) {
	    String key = entry.getKey();
	    Integer value = entry.getValue();

	    formula = formula.replaceFirst(key, Integer.toString(value));
	}

	try {
	    Expression expression = new ExpressionBuilder(formula).build();

	    Integer value = (int) expression.evaluate();
	    riskCalcLogicOutput.put(elementName, value);

	} catch (Exception e) {
//	    System.out.println("\nNot found -->" + elementName);
	}

    }

    public List<RiskCalcLogic> getAll() {
	try {
	    List<RiskCalcLogic> findAll = this.riskCalcLogicRepo.findAll();
	    log.info("Successfully Fetched data from the database risk_calc_logic");
	    return findAll;

	} catch (Exception e) {
	    log.error("Failed to fetch data from risk_calc_logic tbl");
	    log.error(e.getMessage());
	    return new ArrayList<>();

	}

    }

    public void deleteByElementName(String elementName) {

	try {
	    RiskCalcLogic data = this.riskCalcLogicRepo.findByElementName(elementName);
	    this.riskCalcLogicRepo.delete(data);
	    log.info("Successfully DELETED from the risk_calc_logic tbl with elementName: {}", elementName);

	} catch (Exception e) {
	    log.error("Failed to delete from risk_calc_logic tbl with elementName: {}", elementName);
	    log.error(e.getMessage());
	}

    }

    public void updateRiskCalcLogic(String elementName, RiskCalcLogicRequest request) {

	try {
	    RiskCalcLogic data = this.riskCalcLogicRepo.findByElementName(elementName);
//	    data.setElementName(request.getElementName());
	    data.setFormula(request.getFormula());

	    this.riskCalcLogicRepo.save(data	);
	    log.info("Successfully UPDATED in the risk_calc_logic tbl with elementName: {}, and body: {}", elementName,
		    request);

	} catch (Exception e) {
	    log.error("Failed to UPDATE in risk_calc_logic tbl with elementName: {}", elementName);
	    log.error(e.getMessage());
	}

    }
}
