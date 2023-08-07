package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.FormulaNode;
import com.usama.calculatorproblemv2.entity.RiskCalcLogic;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.entity.RiskParameters;
import com.usama.calculatorproblemv2.repo.RiskCalcLogicCoreRepo;
import com.usama.calculatorproblemv2.repo.RiskCalcLogicRepo;
import com.usama.calculatorproblemv2.repo.ScoreCapRepo;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

@Service
public class RiskCalcLogicServiceHelper {

    private final CompanyParameterScoreService companyRiskScoreService;
    private final RiskDimensionService riskDimensionService;
    private final RiskCalcLogicRepo riskCalcLogicRepo;
    private final RiskScoreLevelService riskScoreLevelService;
    private final ScoreCapRepo scoreCapRepo;
    private final RiskCalcLogicCoreRepo riskCalcLogicCoreRepo;

    // new addition
    private Map<String, FormulaNode> formulaMap;

    public RiskCalcLogicServiceHelper(CompanyParameterScoreService companyRiskScoreService,
	    RiskDimensionService riskDimensionService, RiskCalcLogicRepo riskCalcLogicRepo,
	    RiskScoreLevelService riskScoreLevelService, ScoreCapRepo scoreCapRepo,
	    RiskCalcLogicCoreRepo riskCalcLogicCore, RiskCalcLogicCoreRepo riskCalcLogicCoreRepo) {
	this.companyRiskScoreService = companyRiskScoreService;
	this.riskDimensionService = riskDimensionService;
	this.riskCalcLogicRepo = riskCalcLogicRepo;
	this.riskScoreLevelService = riskScoreLevelService;
	this.scoreCapRepo = scoreCapRepo;
	this.riskCalcLogicCoreRepo = riskCalcLogicCoreRepo;

    }

    public boolean checkFormulaNameExists(String str) {

	boolean anyMatch = this.riskCalcLogicRepo.findAll().stream().map(data -> data.getElementName())
		.anyMatch(t -> t.equalsIgnoreCase(str));

	return anyMatch;
    }

    public Map<String, Double> saveVariableValueInMap(Map<String, Double> resultMap, String str, String companyName) {

	Set<RiskParameters> allParametersAndValueOfCompany = GetDimensionNameAndValueOfCompany(companyName);
	List<RiskDimension> allDimensionsAndWeight = getAllDimensionsAndWeight();

	// check in allParametersAndValueOfCompany if Str exists? if yes: then save the
	// key value in resultMap

	for (RiskParameters params : allParametersAndValueOfCompany) {
	    if (params.getParameterName().equalsIgnoreCase(str)) {
		resultMap.put(params.getParameterName(), (double) params.getParameterValue());
		break;
	    }
	}
	for (RiskDimension dimension : allDimensionsAndWeight) {
	    if (dimension.getDimension().equalsIgnoreCase(str)) {
		resultMap.put(dimension.getDimension(), (double) dimension.getWeight());
		break;
	    }
	}

	return resultMap;
    }

    /**
     * @return
     * 
     */
    public List<RiskDimension> getAllDimensionsAndWeight() {
	List<RiskDimension> allDimensionsAndWeight = riskDimensionService.getAllDimensionsAndWeight();
	return allDimensionsAndWeight;
    }

    /**
     * @param companyName
     * @return
     */
    public Set<RiskParameters> GetDimensionNameAndValueOfCompany(String companyName) {
	return this.companyRiskScoreService.findByCompanyName(companyName).getParameters();
    }

    public String getFormulaValue(String str) {

	Optional<RiskCalcLogic> data = this.riskCalcLogicRepo.findAll().stream()
		.filter(formula -> formula.getElementName().equalsIgnoreCase(str)).findFirst();

	if (data.isPresent())
	    return data.get().getFormula();
	return null;
    }

    public void evaluateFormulaValue(String formulaName, String formulaValue, Map<String, Double> resultMap,
	    Map<String, Double> finalFormulaValueMap) {

	final String regex = "\\w+";

	if (formulaName == null)
	    return;

//	System.out.println("formulaName  " + formulaName);
//	System.out.println("formulaValue  " + formulaValue);
//	System.out.println("resultMap  " + resultMap);

	List<String> extractedFormulaList = formulaValueExtracter(formulaValue, regex);
//	System.out.println("extractedFormulaList "+ extractedFormulaList);

	for (String formula : extractedFormulaList) {
	    if (resultMap.containsKey(formula)) {
		formulaValue = formulaValue.replaceFirst(formula, String.valueOf(resultMap.get(formula)));

	    }
	}

//	for (Map.Entry<String, Double> entry : resultMap.entrySet()) {
//	    formulaValue = formulaValue.replaceFirst(entry.getKey(), String.valueOf(entry.getValue()));
//	}

	// Evaluate the expression
	Function minFunction = new Function("MIN", 2) {
	    @Override
	    public double apply(double... args) {
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < args.length; i++) {
		    min = args[i] < min ? args[i] : min;
		}
		return min;
	    }
	};
	try {

	    System.out.println(formulaValue);
	    Expression exp = new ExpressionBuilder(formulaValue).functions(minFunction).build();
	    double evaluate = exp.evaluate();
	    resultMap.put(formulaName, evaluate);
	    finalFormulaValueMap.put(formulaName, evaluate);

	} catch (Exception e) {
	    System.out.println(
		    "Unable to evaluate the formula putting formulaName's value as '0'  " + formulaName + "\n");
	    resultMap.put(formulaName, 0.0);
//	    resultMap.put(formulaName, 0.0);

	}

//	return resultMap;
    }

    private List<String> formulaValueExtracter(String input, String regex) {
	List<String> matches = new ArrayList<>();

	// Create a Pattern object using the provided regex
	Pattern pattern = Pattern.compile(regex);

	// Create a Matcher object to find matches in the input string
	Matcher matcher = pattern.matcher(input);

	// Iterate through the matches and add them to the list
	while (matcher.find()) {
	    String match = matcher.group();
	    matches.add(match);
	}

	return matches;
    }

}
