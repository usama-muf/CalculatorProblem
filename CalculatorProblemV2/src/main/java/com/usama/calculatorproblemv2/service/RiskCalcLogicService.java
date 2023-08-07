package com.usama.calculatorproblemv2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import com.usama.calculatorproblemv2.FormulaNode;
import com.usama.calculatorproblemv2.LoggingUtils;
import com.usama.calculatorproblemv2.dto.RiskCalcLogicRequest;
import com.usama.calculatorproblemv2.entity.CompanyParameterScore;
import com.usama.calculatorproblemv2.entity.RiskCalcLogic;
import com.usama.calculatorproblemv2.entity.RiskCalcLogicCore;
import com.usama.calculatorproblemv2.entity.RiskDimension;
import com.usama.calculatorproblemv2.entity.RiskParameters;
import com.usama.calculatorproblemv2.repo.RiskCalcLogicCoreRepo;
import com.usama.calculatorproblemv2.repo.RiskCalcLogicRepo;
import com.usama.calculatorproblemv2.repo.RiskScoreLevelRepo;
import com.usama.calculatorproblemv2.repo.ScoreCapRepo;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

@Service
public class RiskCalcLogicService {

    private final CompanyParameterScoreService companyRiskScoreService;
    private final RiskDimensionService riskDimensionService;
    private final RiskCalcLogicRepo riskCalcLogicRepo;
    private final RiskScoreLevelService riskScoreLevelService;
    private final ScoreCapRepo scoreCapRepo;
    private final RiskCalcLogicCoreRepo riskCalcLogicCoreRepo;

    @Autowired
    private RiskCalcLogicServiceHelper calcLogicServiceHelper;

    // new addition
    private Map<String, FormulaNode> formulaMap;

    public RiskCalcLogicService(CompanyParameterScoreService companyRiskScoreService,
	    RiskDimensionService riskDimensionService, RiskCalcLogicRepo riskCalcLogicRepo,
	    RiskScoreLevelService riskScoreLevelService, ScoreCapRepo scoreCapRepo,
	    RiskCalcLogicCoreRepo riskCalcLogicCore, RiskCalcLogicCoreRepo riskCalcLogicCoreRepo) {
	this.companyRiskScoreService = companyRiskScoreService;
	this.riskDimensionService = riskDimensionService;
	this.riskCalcLogicRepo = riskCalcLogicRepo;
	this.riskScoreLevelService = riskScoreLevelService;
	this.scoreCapRepo = scoreCapRepo;
	this.riskCalcLogicCoreRepo = riskCalcLogicCoreRepo;

	formulaMap = new HashMap<>();

	// Fetch the data from the "risk_calc_logic" table and create FormulaNode
	// objects
	List<FormulaNode> formulaNodes = this.riskCalcLogicRepo.findAll().stream().map(calcLogic -> {
	    FormulaNode node = new FormulaNode(calcLogic.getElementName(), calcLogic.getFormula());
	    return node;
	}).collect(Collectors.toList());

	// Populate the formulaMap with the FormulaNode objects
	for (FormulaNode node : formulaNodes) {
	    formulaMap.put(node.getElementName(), node);
	}

    }

    Logger log = LoggingUtils.logger;

    // Add new formula to tbl
    public void addNewRiskCalcLogic(RiskCalcLogicRequest request) {

	try {

	    this.riskCalcLogicRepo.save(convertRequestToEntity(request));
	    log.info("Data saved Successfully in the table Risk_Calc_logic with body {}:", request);

	} catch (Exception e) {
	    log.error("Error saving data in risk_cal_logic");
	}
    }

    // Returns list of elementsName/formulaName from RiskCalcLogic tbl
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

    public List<RiskCalcLogicCore> getAllCore() {
	try {
	    List<RiskCalcLogicCore> findAll = this.riskCalcLogicCoreRepo.findAll();
	    log.info("Successfully Fetched data from the database risk_calc_logic_core");
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
//	    System.out.println(data);
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

	    this.riskCalcLogicRepo.save(data);
	    log.info("Successfully UPDATED in the risk_calc_logic tbl with elementName: {}, and body: {}", elementName,
		    request);

	} catch (Exception e) {
	    log.error("Failed to UPDATE in risk_calc_logic tbl with elementName: {}", elementName);
	    log.error(e.getMessage());
	}

    }

    // method return key value pair of company dimensions (dimensionName,
    // dimensionValue)
    public Map<String, Integer> getCompanyParameterAndScoreData(String companyName) {

//	List<Object[]> parameters = this.companyRiskScoreService.findByCompanyName(companyName);
	CompanyParameterScore params = this.companyRiskScoreService.findByCompanyName(companyName);

	Set<RiskParameters> parameters = params.getParameters();
	Map<String, Integer> companyParameterScore = new HashMap<>();

	if (parameters != null)
	    companyParameterScore = parameters.stream()
		    .collect(Collectors.toMap(param -> param.getParameterName(), param -> param.getParameterValue()));

//	System.out.println("getCompanyParameterAndScoreData:: "+companyName +":  "+ companyParameterScore);

	return companyParameterScore;
    }

    // method return key value pair of all dimensions and its weight(dimensionName,
    // dimensionWeight)
    public Map<String, Integer> getRiskDimensionsAndWeight() {
	List<RiskDimension> riskDimensonValues = this.riskDimensionService.getAllDimensionsAndWeight();

	Map<String, Integer> riskDimensionAndWeight = riskDimensonValues.stream()
		.collect(Collectors.toMap(entry -> entry.getDimension(), entry -> entry.getWeight()));

//	System.out.println("getRiskDimensionsAndWeight "+ riskDimensionAndWeight);

	return riskDimensionAndWeight;

    }

    public void calculateOutput() {

	// 1) Fetching Company's Name from company Score Dimension tbl and saving it
	// into a list

	List<String> allCompanyNames = this.companyRiskScoreService.findAll().stream()
		.map(company -> company.getCompanyName()).collect(Collectors.toList());

//	

	allCompanyNames.stream().forEach(companyName -> calculateFormula(companyName));// , allFormulas,
										       // riskDimensionAndWeight));

    }

    public Map<String, Double> calculateFormula(String companyName) {
	// Fetching all basic formulas from risk_calc_logic table
	List<RiskCalcLogic> allFormulas = this.riskCalcLogicRepo.findAll();

	// Fetching all dimensions with their weight
	Map<String, Integer> riskDimensionAndWeight = getRiskDimensionsAndWeight();

	// Storing the ouput in a map.
	Map<String, Double> riskCalcLogicOutput = new HashMap<>();

	return calculateFormula(companyName, allFormulas, riskDimensionAndWeight, riskCalcLogicOutput);

    }

    // calculating the formula
    public Map<String, Double> calculateFormula(String companyName, List<RiskCalcLogic> allFormulas,
	    Map<String, Integer> riskDimensionAndWeight, Map<String, Double> riskCalcLogicOutput) {

	List<String> sortResultCalculationOrder = sortResultCalculationOrder(allFormulas, companyName);
	Map<String, Double> calculationOrderResultMap = calculationOrderResultMap(sortResultCalculationOrder,
		companyName);

	return calculationOrderResultMap;

    }

    // sorting all the formulas accordingly, so that it can be calculated
    private List<String> sortResultCalculationOrder(List<RiskCalcLogic> calcLogicData, String companyName) {
	Map<String, List<String>> graph = new HashMap<>();
	Map<String, String> calcLogic = calcLogicData.stream()
		.collect(Collectors.toMap(entity -> entity.getElementName(), entity -> entity.getFormula()));

	// Construct the dependency graph
	for (Map.Entry<String, String> entry : calcLogic.entrySet()) {
	    String variableName = entry.getKey();
	    String expression = entry.getValue();
	    List<String> dependentVariables = findDependentVariables(expression);
	    graph.put(variableName, dependentVariables);
	}
	// Perform topological sort
	List<String> calculationOrder = topologicalSort(graph);
	System.out.println("Graph: -> " + graph);
	System.out.println("Sort Order :-> " + calculationOrder);
	return calculationOrder;
    }

    // this method takes help of claLogicServiceHelper class and return final
    // formula-value map
    private Map<String, Double> calculationOrderResultMap(List<String> calculationOrder, String companyName) {

	Map<String, Double> resultMap = new HashMap<>();
	Map<String, Double> finalFormulaValueMap = new HashMap<>();

	calculateTotalRiskCappedScore(resultMap, companyName, finalFormulaValueMap);

	for (String str : calculationOrder) {

	    // check if string is FormulaName in RiskCalcLogicTablec
	    boolean isStrFormulaName = this.calcLogicServiceHelper.checkFormulaNameExists(str);
	    // if yes ..
	    if (isStrFormulaName) {
		// here str is formulaName
		// 1) then get the formula value
		String formulaValue = this.calcLogicServiceHelper.getFormulaValue(str);
		this.calcLogicServiceHelper.evaluateFormulaValue(str, formulaValue, resultMap, finalFormulaValueMap);

//and replace the values with actulal variable values

	    }
	    // NO :
	    else {
		// save the variable values in a map for further use.
		resultMap = this.calcLogicServiceHelper.saveVariableValueInMap(resultMap, str, companyName);
	    }
	}
//	return resultMap;
	return finalFormulaValueMap;
    }

    // helps in extracting formula variables from fromula expression
    private static List<String> findDependentVariables(String expression) {
	List<String> dependentVariables = new ArrayList<>();
	String regex = "[+\\-*/()]|\\bMIN\\b";

	String[] tokens = expression.split(regex);
	for (String token : tokens) {
	    token = token.trim();
	    if (!token.isEmpty() && Character.isLetter(token.charAt(0))) {
		dependentVariables.add(token);
	    }
	}
	return dependentVariables;
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph) {
	List<String> calculationOrder = new ArrayList<>();
	Set<String> visited = new HashSet<>();
	Set<String> recursionStack = new HashSet<>();

	for (String variable : graph.keySet()) {
	    if (!visited.contains(variable)) {
		topologicalSortUtil(variable, graph, visited, recursionStack, calculationOrder);
	    }
	}

//	    Collections.reverse(calculationOrder);
	return calculationOrder;
    }

    private static void topologicalSortUtil(String variable, Map<String, List<String>> graph, Set<String> visited,
	    Set<String> recursionStack, List<String> calculationOrder) {
	visited.add(variable);
	recursionStack.add(variable);
	List<String> dependents = graph.get(variable);
	if (dependents != null) {
	    for (String dependent : dependents) {
		if (recursionStack.contains(dependent)) {
		    throw new RuntimeException("Circular dependency detected: " + variable + " -> " + dependent);
		}
		if (!visited.contains(dependent)) {
		    topologicalSortUtil(dependent, graph, visited, recursionStack, calculationOrder);
		}
	    }
	}
	recursionStack.remove(variable);
	calculationOrder.add(variable);
    }

    private void calculateTotalRiskCappedScore(Map<String, Double> riskCalcLogicOutput, String companyName,
	    Map<String, Double> finalFormulaValueMap) {

	// Getting all parameters of companyName
	Map<String, Integer> companysParameterAndScoreData = getCompanyParameterAndScoreData(companyName);

	// this map stores the Level(risk Score level) and its count
	Map<String, Integer> levelCtr = new HashMap<>();

	// loop through each parameter and find its Level using riskScoreLevel table.
	// i.e. take companyDimension value and search it in riskScoreLevel tbl and
	// return the concerned level
	for (Map.Entry<String, Integer> entry : companysParameterAndScoreData.entrySet()) {

	    String levelName = this.riskScoreLevelService.getLevelNamefromValue(entry.getValue());

	    // store this levelByScore in a Map with its count.
	    levelCtr.put(levelName, levelCtr.getOrDefault(levelName, 0) + 1);
	}

	// using this levelCtr map and score_cap table find the totalRiskCappedScore.
	Integer finalRiskCappedScore = Integer.MAX_VALUE;

	for (Map.Entry<String, Integer> entry : levelCtr.entrySet()) {
	    String level = entry.getKey();
	    Integer counter = entry.getValue();
	    Integer totalRiskCappedScore = this.scoreCapRepo.findTotalRiskCappedScore(level, counter);
	    finalRiskCappedScore = Math.min(finalRiskCappedScore, totalRiskCappedScore);
	}

	riskCalcLogicOutput.put("total_risk_capped_score", (double) finalRiskCappedScore);
//	finalFormulaValueMap.put("total_risk_capped_score", (double) finalRiskCappedScore);

//	System.out.println("total risk cpped score OUTPUT for company "+companyName +" "+riskCalcLogicOutput);

    }

    private boolean isElementPresentInLists(String element, List<String> allFormulaNames,
	    List<String> allCompanyParamsName, List<String> allDimensionsName) {
	return allFormulaNames.contains(element) || allCompanyParamsName.contains(element)
		|| allDimensionsName.contains(element);
    }

    // check if formula is valid or not
    public boolean checkFormulaValidity(String formula) {
	List<String> dependentVariables = findDependentVariables(formula);

	List<String> allFormulaNames = riskCalcLogicRepo.findAll().stream().map(logic -> logic.getElementName())
		.collect(Collectors.toList());
	List<String> allCompanyParamsName = companyRiskScoreService.findUniqueParametersName();
	List<String> allDimensionsName = riskDimensionService.getAllDimensionsName();

	// Check if all dependentVariables are present in any of the three lists
	boolean allDependentVariablesPresent = dependentVariables.stream().allMatch(
		element -> isElementPresentInLists(element, allFormulaNames, allCompanyParamsName, allDimensionsName));

	return allDependentVariablesPresent;
    }

    private RiskCalcLogic convertRequestToEntity(RiskCalcLogicRequest request) {
	RiskCalcLogic entity = new RiskCalcLogic();
	entity.setElementName(request.getElementName());
	entity.setFormula(request.getFormula());
	return entity;
    }

}


































// sending whole formula table
//private Map<String, Double> evaluateVariables(List<RiskCalcLogic> calcLogicData, String companyName) {
//	Map<String, List<String>> graph = new HashMap<>();
//	Map<String, Double> variables = new HashMap<>();
//	Map<String, String> calcLogic = calcLogicData.stream()
//		.collect(Collectors.toMap(entity -> entity.getElementName(), entity -> entity.getFormula()));
//
//	// Construct the dependency graph
//	for (Map.Entry<String, String> entry : calcLogic.entrySet()) {
//	    String variableName = entry.getKey();
//	    String expression = entry.getValue();
//	    List<String> dependentVariables = findDependentVariables(expression);
//	    graph.put(variableName, dependentVariables);
//	}
//	// Perform topological sort
//	List<String> calculationOrder = topologicalSort(graph);
////	System.out.println("Graph: -> " + graph);
////	System.out.println("Sort Order :-> " + calculationOrder);
//
//	Map<String, Integer> riskDimensionAndWeight = getRiskDimensionsAndWeight();
//	Map<String, Integer> companysParameterAndScoreData = getCompanyParameterAndScoreData(companyName);
//
//	// Calculate the values in the correct order
//	for (String variable : calculationOrder) {
//	    if (calcLogic.containsKey(variable)) {
//		String formula = calcLogic.get(variable);
//		double value = evaluateExpression(formula, variables, riskDimensionAndWeight,
//			companysParameterAndScoreData);
//		variables.put(variable, value);
//	    } else
//		variables.put(variable, 0.0);
//	}
////	calculateTotalRiskCappedScore(variables, companyName);
//
//	return variables;
//}

//private void saveToMap(Map<String, Double> evalueatedFormulas, Map<String, Double> riskCalcLogicOutput,
//String elementName) {
//
////System.out.println("ElementName : " + elementName);
////System.out.println(evalueatedFormulas+ "\n Saving to riskCallogoutput map "+ riskCalcLogicOutput +" ");
//riskCalcLogicOutput.put(elementName, evalueatedFormulas.get(elementName));
//
//}

//private double evaluateExpression(String formula, Map<String, Double> variables,
//Map<String, Integer> riskDimensionAndWeight, Map<String, Integer> companysParameterAndScoreData) {
//
////boolean formulaPossible = false;
//boolean formulaPossible = checkFormulaValidity(formula);
//System.out.print("\nThis is formula: " + formula);
//
//if (formula != null) {
//
//for (Map.Entry<String, Integer> entry : companysParameterAndScoreData.entrySet()) {
//	formula = formula.replaceFirst(entry.getKey(), String.valueOf(entry.getValue()));
////	if(!formula.equalsIgnoreCase(checkFormula)) formulaPossible=true;
//}
//for (Map.Entry<String, Integer> entry : riskDimensionAndWeight.entrySet()) {
//	formula = formula.replaceFirst(entry.getKey(), String.valueOf(entry.getValue()));
////	if(!formula.equalsIgnoreCase(checkFormula)) formulaPossible=true;
//}
//for (Map.Entry<String, Double> entry : variables.entrySet()) {
//	formula = formula.replaceFirst(entry.getKey(), String.valueOf(entry.getValue()));
////	if(!formula.equalsIgnoreCase(checkFormula)) formulaPossible=true;
//
//}
//System.out.print("\nMap of Variables: " + variables + "\n map of RiskDimensionAndWeigt : "
//	    + riskDimensionAndWeight + "\n map of companyParameandScoreData :   "
//	    + companysParameterAndScoreData + "\n Formula possible ?:" + formulaPossible);
//
//}
//// Evaluate the expression
//
//Function minFunction = new Function("MIN", 2) {
//@Override
//public double apply(double... args) {
//	double min = Double.MAX_VALUE;
//	for (int i = 0; i < args.length; i++) {
//	    min = args[i] < min ? args[i] : min;
//	}
//	return min;
//}
//};
//try {
//
////System.out.println("Expression -> " + formula + "  variables: -> " + variables);
//Expression exp = new ExpressionBuilder(formula).functions(minFunction).build();
//
//double evaluate = exp.evaluate();
//System.out.println("\n this is the value of the formula: " + evaluate);
//return evaluate;
//
//} catch (Exception e) {
//// TODO: handle exception
////System.out.println("Exception :: -> " + e.getMessage());
//System.out.println("\n Unable to evaluate the formula");
//return 0;
//
//}
//}
//private RiskCalcLogicCore convertCoreRequestToEntity(RiskCalcLogicRequest request) {
//	RiskCalcLogicCore entity = new RiskCalcLogicCore();
//	entity.setElementName(request.getElementName());
//	entity.setFormula(request.getFormula());
//	return entity;
//}
