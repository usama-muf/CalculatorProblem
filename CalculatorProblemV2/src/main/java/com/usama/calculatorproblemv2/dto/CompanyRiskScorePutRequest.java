package com.usama.calculatorproblemv2.dto;

import java.util.Map;

public class CompanyRiskScorePutRequest {

    private Map<String, Integer> parameters;

    public Map<String, Integer> getParameters() {
	return parameters;
    }

    public void setParameters(Map<String, Integer> parameters) {
	this.parameters = parameters;
    }

    @Override
    public String toString() {
	return "CompanyRiskScorePutRequest [parameters=" + parameters + "]";
    }

}
