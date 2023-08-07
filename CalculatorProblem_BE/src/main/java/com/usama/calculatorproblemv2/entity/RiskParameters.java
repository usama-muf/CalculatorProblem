package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Embeddable
public class RiskParameters {


//	private String companyRiskScoreId;

	private String parameterName;
	private Integer parameterValue;

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public Integer getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Integer parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Override
	public String toString() {
	    return "RiskParameters [parameterName=" + parameterName + ", parameterValue=" + parameterValue + "]";
	}

	
	
}
