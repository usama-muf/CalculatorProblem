package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RiskCalcLogic {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false )
	private String elementName;

	private String formula;

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

	@Override
	public String toString() {
	    return "RiskCalcLogic [id=" + id + ", elementName=" + elementName + ", formula=" + formula + "]";
	}

	
}
