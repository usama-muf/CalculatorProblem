package com.usama.calculatorproblemv2.dto;

public class RiskCalcLogicRequest {

	private String elementName;
	private String formula;

	public RiskCalcLogicRequest() {	}

	public RiskCalcLogicRequest(String elementName, String formula) {
		super();
		this.elementName = elementName;
		this.formula = formula;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

}
