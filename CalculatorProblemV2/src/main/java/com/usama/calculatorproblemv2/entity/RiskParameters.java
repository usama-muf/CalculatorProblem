package com.usama.calculatorproblemv2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class RiskParameters {

    @NotNull(message = "Name cannot be null")
    private String parameterName;
    
    @Min(value = 0, message = "Company Dimension value cannot be negative")
    @Max(value = 100, message = "Company Dimension value cannot be greater than 100")
    @Column(nullable = false)
    private Integer parameterValue;

    public RiskParameters() {    }

    public RiskParameters(String parameterName, Integer parameterValue) {
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

    public void setParameterValue(Integer integer) {
	this.parameterValue = integer;
    }

    @Override
    public String toString() {
	return "RiskParameters [parameterName=" + parameterName + ", parameterValue=" + parameterValue + "]";
    }

}
