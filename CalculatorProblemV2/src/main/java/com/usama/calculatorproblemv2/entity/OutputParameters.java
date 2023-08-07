package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class OutputParameters {

	private String parameterName;
	private Integer parameterValue;

	public OutputParameters() {
	}

	public OutputParameters(String parameterName, Integer parameterValue) {
		super();
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}

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

}
