package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class OutputParameters {

    private String parameterName;
    private Double parameterValue;

    public OutputParameters() {
    }

    public OutputParameters(String parameterName, Double parameterValue) {
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

    public Double getParameterValue() {
	return parameterValue;
    }

    public void setParameterValue(Double parameterValue) {
	this.parameterValue = parameterValue;
    }

}
