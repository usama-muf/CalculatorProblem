package com.usama.calculatorproblemv2.dto;

public class RiskCalcLogicRequest {

    private String elementName;
    private String formula;
    private boolean isDependentOnOtherFormula ;

    public RiskCalcLogicRequest() {
    }

//    public RiskCalcLogicRequest(String elementName, String formula, boolean isDependentOnOtherFormula) {
//	super();
//	this.elementName = elementName;
//	this.formula = formula;
//	this.isDependentOnOtherFormula = isDependentOnOtherFormula;
//    }

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

    public boolean getIsDependentOnOtherFormula() {
	return isDependentOnOtherFormula;
    }

    public void setDependentOnOtherFormula(boolean isDependentOnOtherFormula) {
	this.isDependentOnOtherFormula = isDependentOnOtherFormula;
    }

    @Override
    public String toString() {
	return "RiskCalcLogicRequest [elementName=" + elementName + ", formula=" + formula
		+ ", isDependentOnOtherFormula=" + isDependentOnOtherFormula + "]";
    }

}
