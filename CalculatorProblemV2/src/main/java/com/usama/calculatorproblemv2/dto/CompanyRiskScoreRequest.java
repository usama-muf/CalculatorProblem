package com.usama.calculatorproblemv2.dto;

import java.util.Map;

public class CompanyRiskScoreRequest {

    private String companyName;
//	private List<Map<String, Integer>> parameters;
    private Map<String, Integer> parameters;

    @Override
    public String toString() {
	return "CompanyRiskScoreRequest [companyName=" + companyName + ", parameters=" + parameters + "]";
    }

    public String getCompanyName() {
	return companyName;
    }

    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    public Map<String, Integer> getParameters() {
	return parameters;
    }

    public void setParameters(Map<String, Integer> parameters) {
	this.parameters = parameters;
    }

}
