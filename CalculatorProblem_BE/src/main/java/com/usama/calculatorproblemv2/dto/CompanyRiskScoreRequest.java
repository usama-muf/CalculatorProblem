package com.usama.calculatorproblemv2.dto;

import java.util.List;
import java.util.Map;

public class CompanyRiskScoreRequest {

	private String companyName;
	private List<Map<String, Integer>> parameters;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<Map<String, Integer>> getParameters() {
		return parameters;
	}

	public void setParameters(List<Map<String, Integer>> parameters) {
		this.parameters = parameters;
	}

}
